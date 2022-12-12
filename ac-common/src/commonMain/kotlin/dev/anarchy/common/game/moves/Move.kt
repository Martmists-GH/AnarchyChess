package dev.anarchy.common.game.moves

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.Position

interface Move {
    val from: Position
    val to: Position
    val isForced: Boolean
    fun execute(board: Board)
}
