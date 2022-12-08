package dev.anarchy.common.game.moves

import dev.anarchy.common.game.Board

interface Move {
    // Square to highlight
    fun targetPosition(): Pair<Int, Int>

    // TODO: How to handle promotion?
    fun execute(board: Board)
}
