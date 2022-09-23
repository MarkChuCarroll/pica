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
package org.goodmath.pica.vm.scheduler;

import org.goodmath.pica.vm.CodeLocation;
import org.goodmath.pica.vm.hadron.QuarkSpec;
import org.goodmath.pica.vm.values.Value;

import java.util.Map;

public record Continuation(
    QuarkSpec owner,
    Map<String, Value> registers,
    CodeLocation entryPoint) {

}