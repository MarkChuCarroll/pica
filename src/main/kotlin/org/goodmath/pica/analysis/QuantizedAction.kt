package org.goodmath.pica.analysis

import org.goodmath.pica.ast.action.Action
import org.goodmath.pica.ast.action.AdoptAction
import org.goodmath.pica.ast.action.TypeAnalysisScope

abstract class QuantizedAction<T: Action>(
    val action: T,
    val enclosingTypeScope: TypeAnalysisScope
) {

}

class QuantizedAdoptAction(a: AdoptAction,
                           s: TypeAnalysisScope): QuantizedAction<AdoptAction>(a, s) {

}

