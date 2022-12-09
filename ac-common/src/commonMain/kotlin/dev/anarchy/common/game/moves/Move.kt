package dev.anarchy.common.game.moves

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.Position

interface Move {
    // Source
    fun sourcePosition(): Position

    // Square to highlight
    fun targetPosition(): Position

    // TODO: How to handle promotion?
    fun execute(board: Board)
}
