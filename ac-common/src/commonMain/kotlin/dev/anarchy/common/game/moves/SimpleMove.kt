package dev.anarchy.common.game.moves

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.Position
import dev.anarchy.common.game.pieces.base.Piece

class SimpleMove(override val from: Position, override val to: Position, override val isForced: Boolean) : Move {
    override fun execute(board: Board) {
        val piece = board.getPiece(from)
        board.setPiece(to, piece)
        board.setPiece(from, Piece.EMPTY)
    }
}
