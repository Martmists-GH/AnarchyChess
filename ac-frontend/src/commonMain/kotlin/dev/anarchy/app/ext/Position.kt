package dev.anarchy.app.ext

import dev.anarchy.common.game.Position

fun Position.isBlack() = (first xor second) and 1 == 0
