package dev.anarchy.common.game.moves

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.Position
import dev.anarchy.common.game.pieces.base.Piece

class PromotionMove(private val from: Position, private val to: Position, private val newPiece: Piece) : Move {
    override fun sourcePosition(): Position {
        return from
    }

    override fun targetPosition(): Position {
        return to
    }

    override fun execute(board: Board) {
        board.setPiece(to, newPiece)
        board.setPiece(from, Piece.EMPTY)
    }
}
