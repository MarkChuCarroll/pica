package org.goodmath.pica.vm.values;

/**
 * Run-time values in Pica.
 *
 * <p>Runtime values include:</p>
 * <ol>
 *     <li> Boson values - either tuples or structs. (Maybe eliminate structs
 *       at runtime? Translate them into tuples?</li>
 *     <li> Quarks. A quark has a quark type, which links to some runtime type
 *       information about the quark.</li>
 *     <li> Channels. A channel has a type, which links to RTTI.</li>
 *     <li> Primitives. Integers, strings, floats.</li>
 *     <li> Continuations. Runtime values containing the quark that owns
 *       the continuation, a register set, and the address of an instruction
 *       to start executing when taken up by a ray.</li>
 * </ol>
 */
public abstract class Value {
    enum VType {
        BosonType,
        QuarkType, ChannelType,
        IntType, FloatType, StringType,
        ContinuationType
    }

    public abstract VType getType();

}
