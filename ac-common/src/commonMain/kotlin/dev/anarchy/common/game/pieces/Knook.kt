package dev.anarchy.common.game.pieces

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.pieces.base.KnightLikePiece
import dev.anarchy.common.game.pieces.base.RookLikePiece

class Knook(override val player: Int) : RookLikePiece, KnightLikePiece {
    override val name = "knook"

    override fun getValidMoves(board: Board): List<Move> {
        val moves = mutableListOf<Move>()
        moves.addAll(getFileMoves(board))
        moves.addAll(getRankMoves(board))
        moves.addAll(getKnightMoves(board))
        return moves
    }

    override fun hasPriority(): Boolean {
        return false
    }
}
