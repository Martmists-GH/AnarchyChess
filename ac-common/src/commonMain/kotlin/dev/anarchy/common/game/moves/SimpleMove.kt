package dev.anarchy.common.game.moves

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.pieces.Piece

class SimpleMove(private val from: Pair<Int, Int>, private val to: Pair<Int, Int>) : Move {
    override fun targetPosition(): Pair<Int, Int> {
        return to
    }

    override fun execute(board: Board) {
        val piece = board.getPiece(from)
        board.setPiece(to, piece)
        board.setPiece(from, Piece.EMPTY)
    }
}
