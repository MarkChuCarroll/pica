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
import { Twist } from "./twist";

export interface TypeBindable<T extends TypeBindable<T>> {
  unboundTypeVars(): TypeVar[];
  bindTypeVars(bindings: Map<TypeVar, SType>): T
}

export abstract class SType extends AstNode implements TypeBindable<SType> {
  constructor(loc: Location) {
    super(loc);
  }

  abstract unboundTypeVars(): TypeVar[];

  abstract bindTypeVars(bindings: Map<TypeVar, SType>): SType;
}

export class NamedType extends SType {
  name: Ident;
  type_args: SType[] = [];

  constructor(loc: Location, name: Ident, type_args?: SType[]) {
    super(loc);
    this.name = name;
    if (type_args) {
      this.type_args = type_args;
    }
  }

  twist(): Twist {
    if (this.type_args.length > 0) {
      return Twist.obj("NamedType",
        Twist.attr("name", this.name.toString()),
        Twist.array("type_args", this.type_args)
      );
    } else {
      return Twist.obj("NamedType",
        Twist.attr("name", this.name.toString()));
    }
  }

  unboundTypeVars(): TypeVar[] {
    let result: TypeVar[] = [];
    for (const ta of this.type_args) {
      result = result.concat(ta.unboundTypeVars());
    }
    return result;
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): NamedType {
    const unBound = this.unboundTypeVars();
    if (Array.from(bindings.keys()).some(unBound.includes)) {
      const boundTypeVars = this.type_args.map((ta) => ta.bindTypeVars(bindings));
      return new NamedType(this.loc, this.name, boundTypeVars);
    } else {
      return this;
    }
  }
}

export class TypeVar extends SType {
  name: Sym;

  constructor(loc: Location, name: Sym) {
    super(loc);
    this.name = name;
  }

  twist(): Twist {
    return Twist.obj("TypeVar", Twist.attr("name", this.name));
  }

  unboundTypeVars(): TypeVar[] {
    return [this];
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): SType {
    if (bindings.has(this)) {
      return bindings.get(this)!;
    } else {
      return this;
    }
  }

}

export type ChannelDir = "in" | "out" | "both";

export class ChannelType extends SType {
  dir: ChannelDir;
  stype: SType;

  constructor(loc: Location, dir: ChannelDir, stype: SType) {
    super(loc);
    this.dir = dir;
    this.stype = stype;
  }

  twist(): Twist {
    return Twist.obj("ChannelType",
      Twist.value("type", this.stype),
      Twist.attr("direction", this.dir));
  }

  unboundTypeVars(): TypeVar[] {
    return this.stype.unboundTypeVars();
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): ChannelType {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new ChannelType(this.loc, this.dir, this.stype.bindTypeVars(bindings));
    } else {
      return this;
    }
  }

}

export class TypedName extends AstNode implements TypeBindable<TypedName> {
  name: Sym;
  stype: SType;

  constructor(loc: Location, name: Sym, stype: SType) {
    super(loc);
    this.name = name;
    this.stype = stype;
  }

  twist(): Twist {
    return Twist.obj("TypedName",
      Twist.attr("name", this.name),
      Twist.value("type", this.stype));
  }

  unboundTypeVars(): TypeVar[] {
    return this.stype.unboundTypeVars();
  }

  bindTypeVars(bindings: Map<TypeVar, SType>): TypedName {
    if (this.unboundTypeVars().some((tv) => bindings.has(tv))) {
      return new TypedName(this.loc, this.name, this.stype.bindTypeVars(bindings));
    } else {
      return this;
    }
  }
}

