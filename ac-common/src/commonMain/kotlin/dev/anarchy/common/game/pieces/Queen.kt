package dev.anarchy.common.game.pieces

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.pieces.base.RookLikePiece
import dev.anarchy.common.game.pieces.base.BishopLikePiece

class Queen(override val player: Int) : RookLikePiece, BishopLikePiece {
    override val name = "queen"

    override fun getValidMoves(board: Board): List<Move> {
        val moves = mutableListOf<Move>()
        moves.addAll(getFileMoves(board))
        moves.addAll(getRankMoves(board))
        moves.addAll(getDiagonalMoves(board))
        return moves
    }

    override fun hasPriority(): Boolean {
        return false
    }
}
