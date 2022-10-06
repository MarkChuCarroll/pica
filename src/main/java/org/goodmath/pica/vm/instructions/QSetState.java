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
package org.goodmath.pica.vm.instructions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.goodmath.pica.vm.Reg;

/**
 * Update the value of a slot in a quark.
 *
 * <pre>
 *     qSetS q, type, name, val
 * </pre>
 */
@AllArgsConstructor
@Getter
public class QSetState extends Instruction {
    private Reg quark;
    private String quarkType;
    private String fieldName;
    private Reg value;

    @Override
    public String toString() {
        return String.format("%sqSetS %s, %s, %s, %s;\n",
                labelMarker(), getQuark().toString(),
                getQuarkType(), getFieldName(), getValue().toString());
    }
}
