package dev.anarchy.common.game.moves

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.Position
import dev.anarchy.common.game.pieces.base.Piece

class EnPassantMove(override val from: Position, override val to: Position, override val isForced: Boolean) : Move {
    override fun execute(board: Board) {
        val piece = board.getPiece(from)
        val player = board.players[piece.player]
        // TODO: use player.forwardDirection for en passant for e.g. 4-player
        board.setPiece(to, piece)
        board.setPiece(from, Piece.EMPTY)
        board.setPiece(Position(to.first, from.second), Piece.EMPTY)
    }
}
