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

import { Action } from "./actions";
import { AstNode, Location, type Sym } from "./ast";
import { Expr } from "./exprs";
import { ChannelType, SType, type TypeBindable, TypedName, TypeVar } from "./stypes";
import { Twist } from "./twist";

export abstract class Definition extends AstNode implements TypeBindable<Definition> {
  name: Sym;

  constructor(loc: Location, name: Sym) {
    super(loc)
    this.name = name;
  }

  abstract unboundTypeVars(): TypeVar[];

  abstract bindTypeVars(bindings: Map<TypeVar, SType>): Definition;
}

export class TypeParamSpec extends AstNode implements TypeBindable<TypeParamSpec> {
  name: Sym;
  constraint: SType | null = null;

  constructor(loc: Location, name: Sym, constraint: SType | null = null) {
    super(loc);
    this.name = name;
    if (constraint) {
      this.constraint = constraint;
    }
  }

  unboundTypeVars(): TypeVar[] {
    return this.constraint?.unboundTypeVars() ?? [];
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): TypeParamSpec {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new TypeParamSpec(this.loc, this.name, this.constraint?.bindTypeVars(bindings) ?? null);
    } else {
      return this;
    }
  }

  twist(): Twist {
    return Twist.obj("TypeParameter",
      Twist.attr("name", this.name),
      Twist.value("constraint", this.constraint));
  }
}

export class FlavorDef extends Definition {
  type_params: TypeParamSpec[] | null;
  composes: SType[];
  channels: ChannelDef[];

  constructor(loc: Location, name: Sym, type_params: TypeParamSpec[] | null, composes: SType[], channels: ChannelDef[]) {
    super(loc, name);
    this.type_params = type_params;
    this.composes = composes;
    this.channels = channels;
  }

  twist(): Twist {
    return Twist.obj("FlavorDef",
      Twist.attr("name", this.name),
      Twist.array("type_params", this.type_params),
      Twist.array("composes", this.composes),
      Twist.array("channels", this.channels)
    );
  }

  unboundTypeVars(): TypeVar[] {
    return (this.type_params?.flatMap((tp) => tp.unboundTypeVars()) ?? []).concat(
      this.composes.flatMap((c) => c.unboundTypeVars()),
      this.channels.flatMap((ch) => ch.unboundTypeVars()));
  }


  bindTypeVars(bindings: Map<TypeVar, SType>): FlavorDef {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new FlavorDef(this.loc, this.name, this.type_params?.map((tp) => tp.bindTypeVars(bindings)) ?? null,
        this.composes.map((comp) => comp.bindTypeVars(bindings)),
        this.channels.map((ch) => ch.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }
}

type direction = "in" | "out" | "both"
export const strToDirection = (s: string): direction => {
  if (s == "in" || s == "out" || s == "both") {
    return s;
  } else {
    throw new Error("Invalid direction")
  }
}

export class ChannelDef extends AstNode implements TypeBindable<ChannelDef> {
  name: Sym;
  ctype: ChannelType;

  constructor(loc: Location, name: Sym, ctype: ChannelType) {
    super(loc);
    this.name = name;
    this.ctype = ctype;
  }

  unboundTypeVars(): TypeVar[] {
    return this.ctype.unboundTypeVars();
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): ChannelDef {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new ChannelDef(this.loc, this.name, this.ctype.bindTypeVars(bindings));
    } else {
      return this;
    }
  }

  twist(): Twist {
    return Twist.obj("ChannelDef",
      Twist.attr("name", this.name),
      Twist.value("channelType", this.ctype));
  }
}

export class QuarkDef extends Definition {
  typeParams: TypeParamSpec[];
  valueParams: TypedName[];
  provides: ChannelDef[];
  slots: TypedName[];
  behaviors: BehaviorDef[];
  defaultBeh: [Sym, Expr[]];

  constructor(loc: Location, name: Sym, typeParams: TypeParamSpec[],
    valueParams: TypedName[], provides: ChannelDef[],
    slots: TypedName[], behaviors: BehaviorDef[], defaultBeh: [Sym, Expr[]]) {
    super(loc, name);
    this.typeParams = typeParams;
    this.provides = provides;
    this.valueParams = valueParams;
    this.behaviors = behaviors;
    this.defaultBeh = defaultBeh;
    this.slots = slots;
  }

  twist(): Twist {
    return Twist.obj("QuarkDef",
      Twist.attr("name", this.name),
      Twist.array("typeParams", this.typeParams),
      Twist.array("valueParams", this.valueParams),
      Twist.array("provides", this.provides),
      Twist.array("slots", this.slots),
      Twist.array("behaviors", this.behaviors),
      Twist.obj("initial",
        Twist.attr("name", this.defaultBeh[0]),
        Twist.array("params", this.defaultBeh[1]))
    );
  }

  unboundTypeVars(): TypeVar[] {
    return this.typeParams.flatMap((tp) => tp.unboundTypeVars()).concat(
      this.valueParams.flatMap((vp) => vp.unboundTypeVars()),
      this.provides.flatMap((pr) => pr.unboundTypeVars()),
      this.slots.flatMap((sl) => sl.unboundTypeVars()),
      this.behaviors.flatMap((beh) => beh.unboundTypeVars()),
      this.defaultBeh[1].flatMap((expr) => expr.unboundTypeVars()));
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): QuarkDef {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new QuarkDef(this.loc, this.name,
        this.typeParams.map((tp) => tp.bindTypeVars(bindings)),
        this.valueParams.map((vp) => vp.bindTypeVars(bindings)),
        this.provides.map((pr) => pr.bindTypeVars(bindings)),
        this.slots.map((sl) => sl.bindTypeVars(bindings)),
        this.behaviors.map((beh) => beh.bindTypeVars(bindings)),
        [this.defaultBeh[0], this.defaultBeh[1].map((expr) => expr.bindTypeVars(bindings))]);
    } else {
      return this;
    }
  }

}

export class BehaviorDef extends AstNode implements TypeBindable<BehaviorDef> {
  name: Sym;
  params: TypedName[];
  body: Action[];

  constructor(loc: Location, name: Sym, params: TypedName[], body: Action[]) {
    super(loc);
    this.name = name;
    this.params = params;
    this.body = body;
  }

  unboundTypeVars(): TypeVar[] {
    return this.params.flatMap((p) => p.unboundTypeVars()).concat(
      this.body.flatMap((act) => act.unboundTypeVars()));
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): BehaviorDef {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new BehaviorDef(this.loc, this.name, this.params.map((p) => p.bindTypeVars(bindings)),
        this.body.map((act) => act.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }

  twist(): Twist {
    return Twist.obj("BehaviorDef",
      Twist.attr("name", this.name),
      Twist.array("params", this.params),
      Twist.array("body", this.body));
  }
}

export class BosonDef extends Definition {
  typeParams: TypeParamSpec[];
  options: BosonOptionDef[];

  constructor(loc: Location, name: Sym, typeParams: TypeParamSpec[],
    options: BosonOptionDef[]) {
    super(loc, name);
    this.typeParams = typeParams;
    this.options = options;
  }

  unboundTypeVars(): TypeVar[] {
    return this.typeParams.flatMap((tp) => tp.unboundTypeVars()).concat(
      this.options.flatMap((opt) => opt.unboundTypeVars()));
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): Definition {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new BosonDef(this.loc, this.name, this.typeParams.map((tp) => tp.bindTypeVars(bindings)),
        this.options.map((opt) => opt.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }

  twist(): Twist {
    return Twist.obj("BosonDef",
      Twist.attr("name", this.name),
      Twist.array("type_params", this.typeParams),
      Twist.array("options", this.options));
  }

}

export abstract class BosonOptionDef extends AstNode implements TypeBindable<BosonOptionDef> {
  name: Sym;

  constructor(loc: Location, name: Sym) {
    super(loc);
    this.name = name;
  }

  abstract unboundTypeVars(): TypeVar[];
  abstract bindTypeVars(bindings: Map<TypeVar, SType>): BosonOptionDef;
}

export class BosonStructOptionDef extends BosonOptionDef {
  fields: TypedName[];

  constructor(loc: Location, name: Sym, fields: TypedName[]) {
    super(loc, name);
    this.fields = fields;
  }

  unboundTypeVars(): TypeVar[] {
    return this.fields.flatMap((f) => f.unboundTypeVars());
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): BosonOptionDef {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new BosonStructOptionDef(this.loc, this.name, this.fields.map((f) => f.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }

  twist(): Twist {
    return Twist.obj("BosonStructOption",
      Twist.attr("name", this.name),
      Twist.array("fields", this.fields));
  }
}


export class BosonTupleOptionDef extends BosonOptionDef {
  fields: SType[];
  constructor(loc: Location, name: Sym, fields: SType[]) {
    super(loc, name);
    this.fields = fields;
  }

  unboundTypeVars(): TypeVar[] {
    return this.fields.flatMap((st) => st.unboundTypeVars());
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): BosonOptionDef {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new BosonTupleOptionDef(this.loc, this.name, this.fields.map((f) => f.bindTypeVars(bindings)));
    } else {
      return this;
    }
  }

  twist(): Twist {
    return Twist.obj("BosonTupleOption",
      Twist.attr("name", this.name),
      Twist.array("fields", this.fields));
  }
}
