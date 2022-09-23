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

public abstract class CodeLocation {
    public static class LabeledInstruction extends CodeLocation {
        private final String label;

        public LabeledInstruction(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return getLabel();
        }
    }

    public static class IndexedInstruction extends CodeLocation {
        private final int index;

        public IndexedInstruction(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return "$" + getIndex();
        }
    }

    public static class RelativeInstruction extends CodeLocation {
        private final int offset;

        public RelativeInstruction(int offset) {
            this.offset = offset;
        }

        public int getOffset() {
            return offset;
        }

        @Override
        public String toString() {
            return "" + getOffset();
        }
    }
}
