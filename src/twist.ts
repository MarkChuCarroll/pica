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


export interface Twistable {
  twist(): Twist
}

/**
 * Twist is an easy-to-generate tree structure that can be 
 * rendered as easy-to-read, easy-to-compare text. I tend to 
 * use it all over the place as a way of testing, comparing,
 * and viewing structured data.
 *
 * A twist node can be an object (a structure with 
 * a collection of named fields), an array (a structure
 * with a list of indexed  values), an attribute (a named
 * named string value), or a value (a single named value).
 */
export abstract class Twist implements Twistable {

  static obj(name: string, ...children: Twistable[]): Twist {
    return new ObjNode(name,
      ...children.map(it => it.twist())
    )
  }

  static array(name: string, children: Twistable[] | null): Twist {
    return new ArrayNode(name,
      children?.map((it) => it.twist()) || null
    )
  }

  static value(name: string, value: Twistable | null): Twist {
    return new ValueNode(name, value?.twist())
  }

  static attr(name: string, value: string): Twist {
    return new AttrNode(name, value)
  }

  abstract render(indent: number): string

  i(n: number): string {
    return "   ".repeat(n)
  }

  twist(): Twist {
    return this
  }
}

class ObjNode extends Twist {
  name: string
  children: Twist[]

  constructor(name: string, ...children: Twist[]) {
    super()
    this.name = name
    this.children = children
  }

  override render(indent: number): string {
    var result = ""
    result += this.i(indent)
    result += `(obj ${this.name}`
    const chStr = this.children.map(it =>
      it.render(indent + 1)
    ).filter(it => it.length != 0).join("\n")
    if (chStr.length > 0) {
      result += "\n" + chStr;
    }
    result += ")"
    return result
  }
}

class ArrayNode extends Twist {
  name: string
  children: Twist[] | null

  constructor(name: string, children: Twist[] | null) {
    super()
    this.name = name
    this.children = children
  }

  override render(indent: number): string {
    if (this.children == null || this.children.length == 0) {
      return "";
    }
    var result = ""
    result += this.i(indent)
    result += `[arr ${this.name}\n`
    result += this.children.map(it =>
      it.render(indent + 1)
    ).filter(it => it.length != 0).join("\n")
    result += "]"
    return result
  }

}

class ValueNode extends Twist {
  name: String
  value: Twist | undefined

  constructor(name: string, value: Twist | undefined) {
    super()
    this.name = name
    this.value = value
  }

  render(indent: number): string {
    if (this.value) {
      var result = ""
      result += this.i(indent)
      result += `{val ${this.name}\n`
      result += this.value.render(indent + 1)
      result += "}"
      return result
    } else {
      return ""
    }
  }
}

class AttrNode extends Twist {
  name: String
  value: String

  constructor(name: string, value: string) {
    super()
    this.name = name
    this.value = value
  }

  render(indent: number): string {
    var result = ""
    result += this.i(indent)
    result += `${this.name} = "${this.value}"`
    return result
  }
}

