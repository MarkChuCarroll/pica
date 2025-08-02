/*
 * Copyright 2024 Mark C. Chu-Carroll
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { AstNode, Ident, Location, type Sym } from "./ast";
import { SType, type TypeBindable, TypeVar } from "./stypes";
import { Twist } from "./twist";

export abstract class Expr extends AstNode implements TypeBindable<Expr> {
  abstract unboundTypeVars(): TypeVar[];
  abstract bindTypeVars(bindings: Map<TypeVar, SType>): Expr;
}

export class VariableExpr extends Expr {
  name: Sym;

  constructor(loc: Location, id: Sym) {
    super(loc);
    this.name = id;
  }

  twist(): Twist {
    return Twist.obj("VarExpr", Twist.attr("id", this.name));
  }

  unboundTypeVars(): TypeVar[] {
    return [];
  }

  bindTypeVars(_: Map<TypeVar, SType>): Expr {
    return this;
  }

}

export type Operator = "+" | "-" | "*" | "/" | "==" | "!=" | "<" | "<="
  | ">=" | ">" | "and" | "or" | "not" | "^" | "%";

export const stringToOperator = (s: string): Operator => {
  switch (s) {
    case "+": return "+";
    case "-": return "-";
    case "*": return "*";
    case "/": return "/";
    case "%": return "%";
    case "^": return "^";
    case "==": return "==";
    case "!=": return "!=";
    case "<": return "<";
    case "<=": return "<=";
    case ">": return ">";
    case ">=": return ">=";
    case "and": return "and";
    case "or": return "or";
    case "not": return "not";
    default:
      throw new Error("Invalid operator")
  }
}

export class OperatorExpr extends Expr {
  op: Operator;
  args: Expr[];

  constructor(loc: Location, op: Operator, args: Expr[]) {
    super(loc);
    this.op = op;
    this.args = args;
  }

  twist(): Twist {
    return Twist.obj("OperatorExpr",
      Twist.attr("operator", this.op),
      Twist.array("args", this.args));
  }

  unboundTypeVars(): TypeVar[] {
    return this.args.flatMap((expr) => expr.unboundTypeVars());
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Expr {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new OperatorExpr(this.loc, this.op, this.args.map((expr) => expr.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }
}

export class CreateExpr extends Expr {
  quarkType: SType;
  args: Expr[];

  constructor(loc: Location, qt: SType, args: Expr[]) {
    super(loc);
    this.quarkType = qt;
    this.args = args;
  }

  twist(): Twist {
    return Twist.obj("CreateExpr",
      Twist.value("quark_type", this.quarkType),
      Twist.array("args", this.args));
  }

  unboundTypeVars(): TypeVar[] {
    return this.quarkType.unboundTypeVars().concat(
      this.args.flatMap((expr) => expr.unboundTypeVars())
    );
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Expr {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new CreateExpr(this.loc, this.quarkType.bindTypeVars(bindings),
        this.args.map((expr) => expr.bindTypeVars(bindings)));
    } else {
      return this
    }
  }
}

export class BosonTupleExpr extends Expr {
  tupleName: Ident;
  typeArgs: SType[] | null = null;
  args: Expr[];

  constructor(loc: Location, tupleName: Ident, typeArgs: SType[] | null,
    args: Expr[]) {
    super(loc);
    this.tupleName = tupleName;
    this.typeArgs = typeArgs;
    this.args = args;
  }

  twist(): Twist {
    return Twist.obj("BosonTupleExpr",
      Twist.attr("tuple_name", this.tupleName.toString()),
      Twist.array("type_args", this.typeArgs),
      Twist.array("args", this.args));
  }

  unboundTypeVars(): TypeVar[] {
    return (this.typeArgs?.flatMap((t) => t.unboundTypeVars()) ?? []).concat(
      this.args.flatMap((expr) => expr.unboundTypeVars()));
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Expr {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new BosonTupleExpr(this.loc, this.tupleName,
        this.typeArgs?.map((t) => t.bindTypeVars(bindings)) ?? null,
        this.args.map((expr) => expr.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }
}

export class BosonStructExpr extends Expr {
  tupleName: Ident;
  typeArgs: SType[] | null = null;
  args: [Sym, Expr][];

  constructor(loc: Location, tupleName: Ident, typeArgs: SType[] | null,
    args: [Sym, Expr][]) {
    super(loc);
    this.tupleName = tupleName;
    this.typeArgs = typeArgs;
    this.args = args;
  }

  twist(): Twist {
    return Twist.obj("BosonTupleExpr",
      Twist.attr("tuple_name", this.tupleName.toString()),
      Twist.array("type_args", this.typeArgs),
      Twist.array("args", this.args?.map((f) =>
        Twist.value(f[0], f[1])
      )));
  }

  unboundTypeVars(): TypeVar[] {
    return (this.typeArgs?.flatMap((t) => t.unboundTypeVars()) ?? []).concat(
      this.args.flatMap((field) => field[1].unboundTypeVars())
    );
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Expr {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new BosonStructExpr(this.loc,
        this.tupleName,
        this.typeArgs?.map((t) => t.bindTypeVars(bindings)) ?? null,
        this.args.map((field) => [field[0], field[1].bindTypeVars(bindings)]));
    } else {
      return this;
    }
  }
}

export class LitStringExpr extends Expr {
  value: string;

  constructor(loc: Location, value: string) {
    super(loc);
    this.value = value;
  }

  twist(): Twist {
    return Twist.obj("LitString", Twist.attr("value", this.value));
  }

  unboundTypeVars(): TypeVar[] {
    return [];
  }

  bindTypeVars(_: Map<TypeVar, SType>): Expr {
    return this;
  }
}

export class LitIntExpr extends Expr {
  value: number;

  constructor(loc: Location, value: number) {
    super(loc);
    this.value = value;
  }

  twist(): Twist {
    return Twist.obj("LitInt", Twist.attr("value", this.value.toString()));
  }

  unboundTypeVars(): TypeVar[] {
    return [];
  }

  bindTypeVars(_: Map<TypeVar, SType>): Expr {
    return this;
  }
}

export class LitFloatExpr extends Expr {
  value: number;

  constructor(loc: Location, value: number) {
    super(loc);
    this.value = value;
  }

  twist(): Twist {
    return Twist.obj("LitFloat", Twist.attr("value", this.value.toString()));
  }

  unboundTypeVars(): TypeVar[] {
    return [];
  }

  bindTypeVars(_: Map<TypeVar, SType>): Expr {
    return this;
  }
}

export class LitCharExpr extends Expr {
  value: string;

  constructor(loc: Location, value: string) {
    super(loc);
    this.value = value;
  }

  twist(): Twist {
    return Twist.obj("LitChar", Twist.attr("value", this.value));
  }
  unboundTypeVars(): TypeVar[] {
    return [];
  }

  bindTypeVars(_: Map<TypeVar, SType>): Expr {
    return this;
  }
}

export class ChannelRefExpr extends Expr {
  quarkExpr: Expr;
  channelName: Sym;

  constructor(loc: Location, quarkExpr: Expr, channelName: Sym) {
    super(loc);
    this.quarkExpr = quarkExpr;
    this.channelName = channelName;
  }

  twist(): Twist {
    return Twist.obj("ChannelRef",
      Twist.value("quarkExpr", this.quarkExpr),
      Twist.attr("channelName", this.channelName));
  }

  unboundTypeVars(): TypeVar[] {
    return this.quarkExpr.unboundTypeVars();
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Expr {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new ChannelRefExpr(this.loc,
        this.quarkExpr.bindTypeVars(bindings),
        this.channelName)
    } else {
      return this;
    }
  }
}

