package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.types.Type
import java.security.MessageDigest
import java.util.*

class InstantiationRegistry<T: Definition>(private val instantiator: (T, List<Type>) -> T) {
    private val b64 = Base64.getEncoder()
    private fun instantiationSignature(def: T, instantiationParameters: List<Type>): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(def.twist().toString().toByteArray(Charsets.UTF_8))
        instantiationParameters.forEach { digest.update(it.twist().toString().toByteArray())}
        return b64.encodeToString(digest.digest())
    }

    private val instantiations = HashMap<String, T>()

    /**
     * We want to be sure that we only instantiate once for a given set of parameters,
     * otherwise we could get into trouble with mutually recursive types. This
     * ensures that each distinct instantiation is created once, and then reused
     * if the same parameters occur.
     */
    fun getOrCreateInstantiation(def: T, instantiationParameters: List<Type>): T {
        // I'm sure there's a better way to do this, but this will work, and it's
        // best thing that's coming to mind right now.
        val typeEnv = def.typeParams.zip(instantiationParameters).associate { (typeVar, concreteType) -> typeVar to concreteType}
        val sig = instantiationSignature(def, instantiationParameters)
        return instantiations.computeIfAbsent(sig) {
            instantiator(def, instantiationParameters)
        }

    }
}