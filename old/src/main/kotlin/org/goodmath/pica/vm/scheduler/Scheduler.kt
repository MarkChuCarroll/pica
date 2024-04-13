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
package org.goodmath.pica.vm.scheduler

import org.goodmath.pica.vm.CodeLocation
import org.goodmath.pica.vm.QuarkSpec

class Value

class Blocker {
    enum class BlockType {
        Send, Recv
    }

    var blockType: BlockType? = null
    var channelId = 0
}


data class Continuation(
    val owner: QuarkSpec,
    val registers: Map<String, Value>,
    val entryPoint: CodeLocation
)


/**
 * The Pica Plasma VM scheduler.
 *
 *
 *  A pica program consists of a set of continuations to be executed by
 * the plasma VM. The scheduler provides a queue where continuations can
 * be submitted for execution.
 *
 *
 * When a continuation is submitted, the scheduler looks at the instruction
 * that caused the continuation to be submitted. If that instruction is not
 * a send or receive, then the continuation is immediately moved to the execution
 * queue. If the submitting instruction is a send or receive, then it creates a
 * pending record for the wait condition, and the continuation stays pending until
 * its condition can be satisfied by a matching receive/send.
 *
 *
 *
 *  When a matching send/receieve pair have been identified, the two continuations
 * will be moved to the execution queue.
 *
 *
 *  Not sure yet how to implement this; possible having a record for each
 * channel, where the pending operations are placed by the scheduler. Then when a new
 * continuation is received, that record will be checked for matching entries? The
 * check is pretty easy; the record will contain either send or receive, but not both:
 * if there were both send and receive, they would have been matched and moved to
 * execution. So if you're a send, check the record, and if there's a receive there,
 * you're good to go; and similar for the receive.
 *
 *
 *  The pool of rays should also be managed by the scheduler, based on the
 * execution queue. If rays are asking for something to execute and there isn't
 * anything ready, then the scheduler should stop those rays. But if the
 * pending queue gets too large, then it should start new rays to make
 * better progress.
 *
 */
class Scheduler {
    internal enum class SchedulableType {
        Send, Recv, Sel, Spawn
    }

    /**
     * Schedulable tries to provide an abstraction  for the things that can be submitted
     * for scheduling. Most of the time, a schedulable is just a single continuation;
     * but in the case of a select, then it's a group of mutually exclusive continuations,
     * and in the case of a spawn, it's a group of continuations all of which should be
     * scheduled.
     *
     *
     * For a spawn, that means that its schedulable adds multiple things to be scheduled.
     * For a select, it gets more complicated, because exactly one should be selected for
     * execution. That's the real reason why schedulable exists.
     *
     *
     *  A schedulable provides a mechanism for determining which conditions
     * could unblock a continuation for scheduling, and for making sure that only one
     * of them actually gets scheduled.
     *
     *
     *  With this, we can put a schedulable into the records for all of the
     * channels that are blocking it or one of its options. Once it's been moved
     * for execution, then it gets marked complete - so when other channel
     * records are checked for matching events, this ones record will be discarded.
     */
    interface Schedulable {
        fun blockedBy(): List<Blocker>?
        fun unblock(b: Blocker): Boolean
        val unblockedContinuation: Continuation
        fun executed(): Boolean
    }

    fun submit(s: Schedulable) {}

    fun pendingQueueSize(): Int {
        return 0
    }

    /**
     * For use by ray threads. This gets a continuation that has been scheduled
     * for execution to be run by the ray.
     * @return
     */
    val executableContinuation: Continuation?
        get() = null
}

