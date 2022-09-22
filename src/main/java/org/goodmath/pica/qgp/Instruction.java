package org.goodmath.pica.qgp;

public class Instruction {


    // The fundamental unit of execution is a ray. A ray is a sequence of instructions
    // associated with a quark. Within a ray, the register "self" always contains the
    // ray's quark.


    // == Messaging ops.
    // A messaging op always terminates the action that calls it. The continuation
    // is a pair of a quark and a code address that will run as a ray when
    // the continuation is called. In a ray started by a receive, the top of the stack will be the
    // received boson.


}