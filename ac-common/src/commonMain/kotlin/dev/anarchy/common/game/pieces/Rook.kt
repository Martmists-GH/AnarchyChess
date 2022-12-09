package dev.anarchy.common.game.pieces

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.pieces.base.RookLikePiece

class Rook(override val player: Int) : RookLikePiece {
    override val name = "rook"

    override fun getValidMoves(board: Board): List<Move> {
        val moves = mutableListOf<Move>()
        moves.addAll(getFileMoves(board))
        moves.addAll(getRankMoves(board))
        return moves
    }

    override fun hasPriority(): Boolean {
        return false
    }
}
