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
package org.goodmath.pica.vm;

import java.util.Map;
import java.util.TreeMap;

/**
 * The fundamental unit of execution is a ray. A ray is a sequence of instructions
 * associated with a quark. Within a ray, there's a special "quark" register that
 * always contains the ray's quark.
 *
 * <p>Rays are very lightweight, and are constantly created and destroyed.
 * A messaging op always terminates the action that calls it. The continuation
 * is a pair of a quark and a code address that will run as a ray when
 * the continuation is called.</p>
 *
 * <p> For the initial implementation, the state is a map, which is
 * more heavyweight than it really should be. Eventually, that'll get
 * changed to a lighter weight structure with copy-on-write behavior.</p>
 */
public class Ray {
    private final Quark quark;
    private final Map<String, Reg> state = new TreeMap<>();

    public Ray(Ray creator, Quark q, int instr) {
        state.putAll(creator.state);
        this.quark = q;
    }

    public Quark getQuark() {
        return quark;
    }

    public Map<String, Reg> getState() {
        return state;
    }
}
