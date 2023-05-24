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

package org.goodmath.pica.vm

import org.goodmath.pica.ast.Identifier
import org.goodmath.pica.util.twist.Twist
import org.goodmath.pica.util.twist.Twistable

/**
 * An instruction for the plasma VM.
 *
 * <p> The Plasma VM is a register machine, so all instructions take registers
 * as inputs, and store their results to registers. The overall execution model
 * is based on <em>continuations</em> and <em>rays</em>.</p>
 *
 * <p> A continuation is basically the standard PL version of a continuation - it's
 * a structure that captures everything needed to continue a computation after
 * an interruption. In Pica, that means that it's got a register set, an owner quark,
 * and an instruction address.</p>
 *
 * <p> The scheduler keeps track of a set of inactive continuations,
 * and the messaging operation that they're waiting for. When that condition is
 * satisfied, it moves the continuation to the ready queue, where it can be
 * picked up for execution by a ray.</p>
 *
 * <p> A ray is an execution context that can pick up and run continuations. There are
 * many rays operating concurrently in a Pica program. The scheduler (which is running in
 * its own thread) moves continuations to a ready queue, and as soon as the continuation
 * a ray is running suspends, it takes the next continuation from the queue and starts
 * running it.</p>
 */
abstract class Instruction(
    var label: String?,
): Twistable


open class BinaryInstr(label: String?, val name: String, val target: Reg, val left: Reg, val right: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj(name,
            Twist.opt(label?.let { Twist.attr("label", it)}),
            Twist.value("target", target),
            Twist.value("left", left),
            Twist.value("right", right))

}

/**
 * Logical and: read boolean values from two registers, and store their
 * logical and in a target register.
 *
 * <pre>
 *     and tgt, in1, in2
 * </pre>
 */
class And(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::And", target, left, right)


/**
 * Get a field from a boson.
 *
 * <pre>
 *     bGet tgt, type, field
 * </pre>
 *
 * <ul>
 *     <li> tgt: Reg is a register where the result will be stored.</li>
 *     <li> type: Identifier is the identifier of the boson option type.</li>
 *     <li> field: Int is the index of the field.</li>
 * </ul>
  */
class BGetField(label: String?, val target: Reg, val src: Reg, val field: Int): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::BGetField",
            Twist.opt(label?.let { Twist.attr("label", it)}),
            Twist.value("target", target),
            Twist.value("src", src),
            Twist.attr("field", field.toString()))

}

class IAdd(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::IAnd", target, left, right)

class ISubtract(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::ISub", target, left, right)

class IMult(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::IMult", target, left, right)


class IDiv(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::IDiv", target, left, right)

class IGt(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::IGt", target, left, right)

class IGe(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::IGe", target, left, right)

class ILt(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::ILt", target, left, right)

class ILe(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::ILe", target, left, right)

class IMod(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::IMod", target, left, right)

class INeg(label: String?, target: Reg, src: Reg): Unary(label, "Intr::INeg", target, src)


/**
 * Instruction to create a new boson.
 *
 * <pre>
 *     bNew reg, type, arg0, arg1, ..., argN
 * </pre>
 *
 * <ul>
 *     <li> reg: Reg is the register where the new boson will be stored.</li>
 *     <li> type: Identifier is the identifier of the boson option.</li>
 *     <li> argN: Reg are registers where the  Fields of the new boson are stored.</li>
 * </ul>
 *
 */
class BNew(
    label: String?,
    val target: Reg,
    val bosonType: Identifier,
    val values: List<Reg>
): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::Bnew",
            Twist.opt(label?.let { Twist.attr("label", it) } ),
            Twist.value("target", target),
            Twist.value("bosonType", bosonType),
            Twist.arr("values", values)
        )
}



/**
 * Instruction to create a modified boson. The new boson is equal to the
 * input, except for one updated field.
 *
 * <pre>
 *     bSet tgt, type, field, val
 * </pre>
 *
 * <ol>
 *     <li> tgt: Reg is the register where the new boson value will be stored.</li>
 *     <li> type: Identifier is the identifier of the boson option type.</li>
 *     <li> field: String is the name of the field to update in the new boson.</li>
 *     <li> val: Reg is a register containing the value of the updated field in the new boson.</li>
 * </ol>
 *
 */
class BSetField(
    label: String?,
    val target: Reg,
    val typeName: Identifier,
    val field: Int,
    val source: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::BsetField",
            Twist.opt(label?.let { Twist.attr("label", it) } ),
            Twist.value("target", target),
            Twist.attr("typeName", typeName.toString()),
            Twist.attr("field", field.toString()),
            Twist.value("source", source)
        )
}



/**
 * Branch instruction. The PC for the executing ray will be changed to the target
 * if the condition register is true.
 *
 * <pre>
 *     brIf reg, tgt
 * </pre>
 * <ul>
 *     <li>reg: Reg is the register containing the condition value.</li>
 *     <li>tgt: CodeLocation is the location to branch to if the condition is true.</li>
 * </ul>
 */
class BranchIfTrue(label: String?, val reg: Reg, val loc: CodeLocation): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::BranchIfTrue",
            Twist.opt(label?.let { Twist.attr("label", it) } ),
            Twist.value("flag", reg),
            Twist.value("label", loc))

}


/**
 * Instruction to create a new continuation for the current quark, with a copy of the
 * current ray's local registers, and the address of an instruction to execute when
 * the continuation starts running in a ray.
 *
 * <pre>
 *     cNew reg, loc;
 * </pre>
 *
 * <ul>
 *     <li> reg: Reg a register where the new continuation will be stored.</li>
 *     <li> loc: the code location of the instruction to start executing.</li>
 * </ul>
 */
class CNew(label: String?, val target: Reg, val codeLoc: CodeLocation): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::CNew",
            Twist.opt(label?.let { Twist.attr("label", it) } ),
            Twist.value("target", target),
            Twist.value("codeLoc", codeLoc)
        )
}

abstract class ConvertValue(label: String?, val name: String, val target: Reg, val src: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj(name,
            Twist.opt(label?.let { Twist.attr("label", it) } ),
            Twist.value("target", target),
            Twist.value("source", src)
        )
}

class IToF(label: String?, target: Reg, src: Reg): ConvertValue(label, "Instr::IToF", target, src)
class IToS(label: String?, target: Reg, src: Reg): ConvertValue(label, "Instr::IToS", target, src)
class FToI(label: String?, target: Reg, src: Reg): ConvertValue(label, "Instr::FToI", target, src)
class FToS(label: String?, target: Reg, src: Reg): ConvertValue(label, "Instr::FToS", target, src)
class SToI(label: String?, target: Reg, src: Reg): ConvertValue(label, "Instr::SToI", target, src)
class SToF(label: String?, target: Reg, src: Reg): ConvertValue(label, "Instr::SToF", target, src)


class Eq(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::Eq", target, left, right)

class FAdd(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::FAdd", target, left, right)

class FSub(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::FSub", target, left, right)

class FMult(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::FMult", target, left, right)


class FDiv(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::FDiv", target, left, right)

class FLe(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::FLe", target, left, right)

class FLt(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::FLt", target, left, right)

class FGe(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::Ge", target, left, right)

class FGt(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::FGt", target, left, right)


abstract class Unary(label: String?, val name: String, val target: Reg, val src: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj(name,
            Twist.opt(label?.let { Twist.attr("label", it)}),
            Twist.value("target", target),
            Twist.value("source", src))
}

class FInv(label: String?, target: Reg, src: Reg): Unary(label, "Instr::FInv", target, src)

class FNeg(label: String?, target: Reg, src: Reg): Unary(label, "Instr::FNeg", target, src)



/**
 * Unconditional branch.
 *
 * <pre>
 *     jmp loc;
 * </pre>
 */
class Jmp(label: String?, val codeLoc: CodeLocation): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::Jmp",
            Twist.opt(label?.let { Twist.attr("label", it)}),
            Twist.value("codeLoc", codeLoc)
        )
}

class Not(label: String?, target: Reg, src: Reg): Unary(label, "Instr::Not", target, src)

class Or(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::Or", target, left, right)


/**
 * Get a named channel from a quark.
 *
 * <code>
 *     qGetC tgt, q, type, name;
 * </code>
 *
 * <ul>
 *     <li> tgt: Reg is a register where the channel will be stored.</li>
 *     <li> q: Reg is a register where the quark is stored.</li>
 *     <li> type: Identifier is the quark type.</li>
 *     <li> name: String is the channel name.</li>
 * </ul>
 */
class QGetChannel(label: String?,
                  val quarkType: Identifier,
                  val target: Reg, val src: Reg, val channelName: String): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::QGetC",
            Twist.opt(label?.let { Twist.attr("label", it)}),
            Twist.attr("quarkType", quarkType.toString()),
            Twist.value("target", target),
            Twist.value("source", src),
            Twist.attr("channelName", channelName)
        )
}

/**
 * Get the value of a named slot from a quark.
 *
 * <code>
 *     qGetS tgt, q, type, name;
 * </code>
 *
 * <ul>
 *     <li> tgt: Reg is a register where the slot value will be stored.</li>
 *     <li> q: Reg is a register where the quark is stored.</li>
 *     <li> type: Identifier is the quark type.</li>
 *     <li> name: String is the slot name.</li>
 * </ul>
 */
class QGetState(label: String?,
                val quarkType: Identifier,
                val target: Reg, val src: Reg, val slotName: String): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::QGetS",
            Twist.opt(label?.let { Twist.attr("label", it)}),
            Twist.attr("quarkType", quarkType.toString()),
            Twist.value("target", target),
            Twist.value("source", src),
            Twist.attr("slotName", slotName)
        )
}


/**
 * Create a new quark of a specified type.
 *
 * <pre>
 *     qNew tgt, type, params...
 * </pre>
 * <ul>
 *     <li> tgt: Reg is a register where the new quark will be stored.</li>
 *     <li> type: Identifier is the fully qualified name of the type of the quark.</li>
 *     <li> params: List[Reg] is a list of registers containing the constructor parameters
 *        for the quark.</li>
 * </ul>
 */
class QNew(label: String?,
           val target: Reg, val type: Identifier, val args: List<Reg>): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::QNew",
            Twist.opt(label?.let { Twist.attr("label", it)}),
            Twist.attr("quarkType", type.toString()),
            Twist.value("target", target),
            Twist.arr("args", args)
        )
}


/**
 * Update the value of a slot in a quark.
 *
 * <pre>
 *     qSetS q, type, name, val
 * </pre>
 */
class QSetState(label: String?,
                val quarkType: Identifier,
                val target: Reg, val slotName: String, val src: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::QSetS",
            Twist.opt(label?.let { Twist.attr("label", it)}),
            Twist.value("target", target),
            Twist.attr("slotName", slotName),
            Twist.value("source", src))
}

/**
 * Receive a message on a channel. This suspends the current ray, and places a continuation into
 * the scheduling queue with the code to be executed once the message is received. Note that
 * you can receive from any channel that you have access to; if you have the channel, you can
 * both send and recieve with it.
 *
 * <pre>
 *     recv tgt, chan, cont;
 * </pre>
 *
 * <ul>
 *     <li>tgt: Reg is the register where the received boson value will be stored.</li>
 *     <li>chan: Reg is the register containing the channel to receive the message.</li>
 *     <li>cont: Continuation is the continuation to be scheduled when the message
 *        has been received.</li>
 * </ul>
 */
class Receive(label: String?, val target: Reg, val channel: Reg, val cont: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::Recv",
            Twist.opt(label?.let { Twist.attr("label", it)}),
            Twist.value("target", target),
            Twist.value("channel", channel),
            Twist.value("continuation", cont))
}

class RNew(label: String?, val target: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::RNew",
            Twist.value("target", target))
}


/**
 * Send(channel, reg, cont)
 */
class Send(label: String?, val chan: Reg, val msg: Reg, val cont: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Send",
            Twist.value("channel", chan),
            Twist.value("msg", msg),
            Twist.value("continuation", cont))
}


/**
 * Run exactly one of a set of alternative continuations.
 *
 * <p> All of the continuations <em>must</em> start with either a send or a
 * receive as their first instruction.  The instruction suspends until at
 * least one of the continuations' send or receive operations become unblocked;
 * if more than one is unblocked, one will be selected non-deterministically.
 * (Basically, whichever comes first in the data structures.)</p>
 *
 * <pre>
 *     sel c1, c2, ...
 * </pre>
 */
class Select(label: String?, val conts: List<Reg>): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::Sel",
            Twist.opt(label?.let { Twist.attr("label", it)}),
            Twist.arr("continuations", conts))
}


class SIndex(
    label: String?,
    target: Reg,
    left: Reg,
    right: Reg
): BinaryInstr(label, "Instr::SIdx", target, left, right)

class StrCat(label: String?,
             target: Reg,
             left: Reg,
             right: Reg
): BinaryInstr(label, "Instr::SCat", target, left, right)


class SLen(label: String?,
           val target: Reg,
           val src: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::SLen",
            Twist.opt(label?.let { Twist.attr("label", it) } ),
            Twist.value("target", target),
            Twist.value("source", src))

}

class SGt(label: String?,
           val target: Reg,
           val left: Reg,
          val right: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::SGt",
            Twist.opt(label?.let { Twist.attr("label", it) } ),
            Twist.value("target", target),
            Twist.value("left", left),
            Twist.value("right", right))


}
class SGe(label: String?,
          val target: Reg,
          val left:Reg,
          val right: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::SGe",
            Twist.opt(label?.let { Twist.attr("label", it) } ),
            Twist.value("target", target),
            Twist.value("left", left),
            Twist.value("right", right))


}

class SLt(label: String?,
           val target: Reg,
          val left:Reg,
          val right: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::SLt",
            Twist.opt(label?.let { Twist.attr("label", it) } ),
            Twist.value("target", target),
            Twist.value("left", left),
            Twist.value("right", right))
}

class SLe(label: String?,
           val target: Reg,
          val left:Reg,
          val right: Reg): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::SLe",
            Twist.opt(label?.let { Twist.attr("label", it) } ),
            Twist.value("target", target),
            Twist.value("left", left),
            Twist.value("right", right))
}

/**
 * Start a collection of new continuations. Each of the new continuations starts
 * with a copy of the ray-state of the current continuation. Once this instruction
 * is complete, the new continuations will be in the scheduling queue. Unlike the
 * <code>sel</code> instruction, <code>spawn</code> runs <em>all</em> of the
 * continuations, not just one of them.
 *
 * <pre>
 *     spawn c1, c2, ...;
 * </pre>
 *
 */
class Spawn(label: String?, val continuations: List<Reg>): Instruction(label) {
    override fun twist(): Twist =
        Twist.obj("Instr::Spawn",
            Twist.arr("continuations", continuations))
}

/**
 * Terminate the current ray without a continuation.
 */
class Stop(label: String?): Instruction(label) {
    override fun twist(): Twist =
        Twist.leaf("Instr::Stop")
}

abstract class CodeLocation: Twistable

class NamedCodeLoc(val label: String): CodeLocation() {
    override fun twist(): Twist =
        Twist.attr("namedCodeLoc", label)
}

class IndexedCodeLoc(val loc: Int): CodeLocation() {
    override fun twist(): Twist =
        Twist.attr("indexedCodeLoc", loc.toString())
}

class RelativeCodeLoc(val loc: Int): CodeLocation() {
    override fun twist(): Twist =
        Twist.attr("relativeCodeLoc", loc.toString())
}
