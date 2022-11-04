package org.goodmath.pica.ast.defs

import org.goodmath.pica.ast.AstNode
import org.goodmath.pica.ast.Location
import org.goodmath.pica.util.Symbol

abstract class BosonOption(
    val name: Symbol,
    loc: Location
): AstNode(loc)