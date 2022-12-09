package dev.anarchy.common.game.pieces

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.pieces.base.BishopLikePiece

class Bishop(override val player: Int) : BishopLikePiece {
    override val name = "bishop"

    override fun getValidMoves(board: Board): List<Move> {
        return getDiagonalMoves(board)
    }

    override fun hasPriority(): Boolean {
        return false
    }
}
