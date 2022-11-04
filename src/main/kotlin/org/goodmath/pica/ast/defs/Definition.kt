/* Copyright 2022, Mark C. Chu-Carroll
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

package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.ast.Location
import org.goodmath.pica.ast.types.TypeVar
import org.goodmath.pica.util.Symbol

abstract class Definition(
    val hadronId: Identifier,
    val name: Symbol,
    val typeParams: List<TypeVar>,
    loc: Location
): AstNode(loc) {
    val id = Identifier(hadronId, name, loc)
}

