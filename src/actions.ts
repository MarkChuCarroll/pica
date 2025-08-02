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

import { AstNode, Ident, Location, Sym } from "./ast";
import { Expr } from "./exprs";
import { SType, TypeBindable, TypeVar } from "./stypes";
import { Twist } from "./twist";


export abstract class Action extends AstNode implements TypeBindable<Action> {
  abstract unboundTypeVars(): TypeVar[];
  abstract bindTypeVars(bindings: Map<TypeVar, SType>): Action;
}

export class ParAction extends Action {
  actions: Action[];

  constructor(loc: Location, actions: Action[]) {
    super(loc);
    this.actions = actions;
  }

  twist(): Twist {
    return Twist.obj("Par", Twist.array("actions", this.actions));
  }

  unboundTypeVars(): TypeVar[] {
    return this.actions.flatMap((act) => act.unboundTypeVars());
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Action {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new ParAction(this.loc, this.actions.map((act) => act.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }


}

export class SeqAction extends Action {
  actions: Action[];

  constructor(loc: Location, actions: Action[]) {
    super(loc);
    this.actions = actions;
  }

  twist(): Twist {
    return Twist.obj("Seq", Twist.array("actions", this.actions));
  }

  unboundTypeVars(): TypeVar[] {
    return this.actions.flatMap((act) => act.unboundTypeVars());
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Action {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new SeqAction(this.loc, this.actions.map((act) => act.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }

}

export class SelectAction extends Action {
  actions: Action[];

  constructor(loc: Location, actions: Action[]) {
    super(loc);
    this.actions = actions;
  }

  twist(): Twist {
    return Twist.obj("Select", Twist.array("actions", this.actions));
  }

  unboundTypeVars(): TypeVar[] {
    return this.actions.flatMap((act) => act.unboundTypeVars());
  }


  bindTypeVars(bindings: Map<TypeVar, SType>): Action {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new SelectAction(this.loc, this.actions.map((act) => act.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }
}

export class SendAction extends Action {
  channel: Expr;
  value: Expr;

  constructor(loc: Location, channel: Expr, value: Expr) {
    super(loc);
    this.channel = channel;
    this.value = value;
  }

  twist(): Twist {
    return Twist.obj("Send",
      Twist.value("channel", this.channel),
      Twist.value("value", this.value));
  }

  unboundTypeVars(): TypeVar[] {
    return this.channel.unboundTypeVars().concat(this.value.unboundTypeVars());
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Action {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new SendAction(this.loc, this.channel.bindTypeVars(bindings), this.value.bindTypeVars(bindings));
    } else {
      return this;
    }

  }
}

export abstract class MessagePattern extends AstNode {
  name: Ident;

  constructor(loc: Location, name: Ident) {
    super(loc);
    this.name = name;
  }
}

export class TupleMessagePattern extends MessagePattern {
  fields: Sym[];

  constructor(loc: Location, name: Ident, fields: Sym[]) {
    super(loc, name);
    this.fields = fields;
  }

  twist(): Twist {
    return Twist.obj("TuplePattern",
      Twist.array("fields", this.fields.map((field, idx) => Twist.attr(`field${idx}`, field))));
  }
}

export class StructMessagePattern extends MessagePattern {
  fields: [string, string][];

  constructor(loc: Location, name: Ident, fields: [string, string][]) {
    super(loc, name);
    this.fields = fields;
  }

  twist(): Twist {
    return Twist.obj("StructPattern",
      Twist.array("fields",
        this.fields.map((f) => Twist.attr(f[0], f[1]))
      )
    );
  }
}


export class RecvClause extends AstNode implements TypeBindable<RecvClause> {
  pattern: MessagePattern;
  actions: Action[];

  constructor(loc: Location, pattern: MessagePattern, actions: Action[]) {
    super(loc);
    this.pattern = pattern;
    this.actions = actions;
  }

  twist(): Twist {
    return Twist.obj("RecvClause",
      Twist.value("pattern", this.pattern),
      Twist.array("actions", this.actions)
    );
  }

  unboundTypeVars(): TypeVar[] {
    return this.actions.flatMap((act) => act.unboundTypeVars());
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): RecvClause {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new RecvClause(this.loc, this.pattern, this.actions.map((act) => act.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }
}

export class RecvAction extends Action {
  source: Expr;
  clauses: RecvClause[];
  else_clause: Action[] | null;

  constructor(loc: Location,
    source: Expr,
    clauses: RecvClause[],
    else_clause: Action[] | null) {
    super(loc);
    this.source = source;
    this.clauses = clauses;
    this.else_clause = else_clause;
  }

  twist(): Twist {
    return Twist.obj("Recv",
      Twist.value("fromChannel", this.source),
      Twist.array("clauses", this.clauses),
      Twist.array("else", this.else_clause)
    )
  }

  unboundTypeVars(): TypeVar[] {
    return this.clauses.flatMap((cl) => cl.unboundTypeVars()).concat(
      this.else_clause?.flatMap((act) => act.unboundTypeVars()) ?? [],
      this.source.unboundTypeVars());
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Action {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new RecvAction(this.loc, this.source.bindTypeVars(bindings),
        this.clauses.map((cl) => cl.bindTypeVars(bindings)),
        this.else_clause?.map((act) => act.bindTypeVars(bindings)) ?? null);
    } else {
      return this;
    }
  }
}

export class AssignAction extends Action {
  target: Sym;
  value: Expr;

  constructor(loc: Location, target: Sym, value: Expr) {
    super(loc);
    this.target = target;
    this.value = value;
  }

  twist(): Twist {
    return Twist.obj("Assign",
      Twist.attr("target", this.target),
      Twist.value("value", this.value));
  }

  unboundTypeVars(): TypeVar[] {
    return this.value.unboundTypeVars();
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Action {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new AssignAction(this.loc, this.target, this.value.bindTypeVars(bindings));
    } else {
      return this;
    }
  }
}

export class LocalVarAction extends Action {
  id: Sym;
  stype: SType;
  value: Expr;

  constructor(loc: Location, id: Sym, stype: SType, value: Expr) {
    super(loc);
    this.id = id;
    this.stype = stype;
    this.value = value;
  }

  twist(): Twist {
    return Twist.obj("Local",
      Twist.attr("name", this.id),
      Twist.value("type", this.stype),
      Twist.value("value", this.value)
    );
  }

  unboundTypeVars(): TypeVar[] {
    return this.stype.unboundTypeVars().concat(this.value.unboundTypeVars());
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Action {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new LocalVarAction(this.loc, this.id, this.bindTypeVars(bindings),
        this.stype.bindTypeVars(bindings));
    } else {
      return this;
    }
  }
}

export class CondClause extends AstNode implements TypeBindable<CondClause> {
  expr: Expr;
  actions: Action[];

  constructor(loc: Location, expr: Expr, actions: Action[]) {
    super(loc);
    this.expr = expr;
    this.actions = actions;
  }

  unboundTypeVars(): TypeVar[] {
    return this.expr.unboundTypeVars().concat(this.actions.flatMap((act) => act.unboundTypeVars()));
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): CondClause {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new CondClause(this.loc, this.expr.bindTypeVars(bindings),
        this.actions.map((act) => act.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }

  twist(): Twist {
    return Twist.obj("CondClause",
      Twist.value("condition", this.expr),
      Twist.array("actions", this.actions)
    );
  }
}

export class CondAction extends Action {
  clauses: CondClause[];
  else_clause: Action[];

  constructor(loc: Location, clauses: CondClause[],
    else_clause: Action[]) {
    super(loc);
    this.clauses = clauses;
    this.else_clause = else_clause;
  }

  twist(): Twist {
    return Twist.obj("Cond",
      Twist.array("clauses", this.clauses),
      Twist.array("else", this.else_clause)
    );
  }

  unboundTypeVars(): TypeVar[] {
    return this.clauses.flatMap((cl) =>
      cl.unboundTypeVars())
      .concat(this.else_clause.flatMap((act) => act.unboundTypeVars()));
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Action {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new CondAction(this.loc,
        this.clauses.map((clause) => clause.bindTypeVars(bindings)),
        this.else_clause.map((act) => act.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }
}

export class WhileAction extends Action {
  cond: Expr;
  body: Action[];

  constructor(loc: Location, cond: Expr, body: Action[]) {
    super(loc);
    this.cond = cond;
    this.body = body;
  }

  twist(): Twist {
    return Twist.obj("While",
      Twist.value("cond", this.cond),
      Twist.array("body", this.body));
  }

  unboundTypeVars(): TypeVar[] {
    return this.cond.unboundTypeVars().concat(this.body.flatMap((act) => act.unboundTypeVars()));
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Action {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new WhileAction(this.loc, this.cond.bindTypeVars(bindings),
        this.body.map((act) => act.bindTypeVars(bindings)));
    } else {
      return this
    }
  }
}

export class AdoptAction extends Action {
  newBeh: Sym;
  params: Expr[];

  constructor(loc: Location, newBeh: Sym, params: Expr[]) {
    super(loc);
    this.newBeh = newBeh;
    this.params = params;
  }

  twist(): Twist {
    return Twist.obj("Adopt",
      Twist.attr("behavior", this.newBeh),
      Twist.array("parameters", this.params));
  }

  unboundTypeVars(): TypeVar[] {
    return this.params.flatMap((expr) => expr.unboundTypeVars());
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Action {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new AdoptAction(this.loc, this.newBeh, this.params.map((expr) => expr.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }
}

export class ExitAction extends Action {
  constructor(loc: Location) {
    super(loc);
  }

  twist(): Twist {
    return Twist.obj("Exit");
  }

  unboundTypeVars(): TypeVar[] {
    return [];
  }

  bindTypeVars(_: Map<TypeVar, SType>): Action {
    return this;
  }

}

