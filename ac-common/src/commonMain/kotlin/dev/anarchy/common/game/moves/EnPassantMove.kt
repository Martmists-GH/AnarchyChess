package dev.anarchy.common.game.moves

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.Position
import dev.anarchy.common.game.pieces.base.Piece

class EnPassantMove(val from: Position, val to: Position) : Move {
    override fun sourcePosition(): Position {
        return from
    }

    override fun targetPosition(): Position {
        return to
    }

    override fun execute(board: Board) {
        val piece = board.getPiece(from)
        board.setPiece(to, piece)
        board.setPiece(from, Piece.EMPTY)
        board.setPiece(Position(to.first, from.second), Piece.EMPTY)
    }
}
