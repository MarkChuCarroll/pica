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

import { Definition } from "./defs";
import { Twist, Twistable } from "./twist";

// AST nodes.

/**
 * The location of a syntactic element.
 */
export class Location {
  file: string;
  line: number;
  column: number

  constructor(file: string, line: number, column: number) {
    this.file = file;
    this.line = line;
    this.column = column;
  }
}

export type Sym = string;



export abstract class AstNode implements Twistable {
  loc: Location

  constructor(loc: Location) {
    this.loc = loc;
  }

  abstract twist(): Twist;
}

export class Ident extends AstNode {
  path: Sym[];

  constructor(loc: Location, path: Sym[]) {
    super(loc);
    this.path = path;
  }

  override toString(): string {
    return this.path.join("::");
  }

  twist(): Twist {
    return Twist.obj("Identifier", Twist.attr("name", this.toString()));
  }
}



export class Hadron extends AstNode {
  name: string;
  uses: UseDecl[];
  defs: Definition[];

  constructor(name: string, uses: UseDecl[], defs: Definition[]) {
    super(new Location(name, 0, 0));
    this.name = name;
    this.uses = uses;
    this.defs = defs;
  }

  twist(): Twist {
    return Twist.obj("Hadron",
      Twist.attr("name", this.name),
      Twist.array("uses", this.uses),
      Twist.array("definitions", this.defs)
    )
  }

}

export class UseDecl extends AstNode {
  sectPath: Ident;
  names: Sym[] | null = null;

  constructor(loc: Location, sectPath: Ident, names: Sym[] | null) {
    super(loc);
    this.sectPath = sectPath;
    if (names) {
      this.names = names;
    }
  }

  twist(): Twist {
    return Twist.obj("UseDecl",
      Twist.attr("fromSect", this.sectPath.toString()),
      Twist.array("names", (this.names !== null) ? this.names.map((n) => Twist.attr("name", n)) : null));
  }
}

