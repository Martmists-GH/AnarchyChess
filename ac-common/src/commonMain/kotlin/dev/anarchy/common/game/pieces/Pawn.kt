package dev.anarchy.common.game.pieces

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.Position
import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.moves.SimpleMove
import dev.anarchy.common.game.pieces.base.Piece

class Pawn(override val player: Int) : Piece {
    override val name: String = "pawn"

    override fun getForcedMoves(board: Board, position: Position): List<Move> {
        // TODO:
        // - Stop check
        // - En passant
        return emptyList()
    }

    override fun getUnforcedMoves(board: Board, position: Position): List<Move> {
        // TODO:
        // - Capture diagonally
        // - En passant
        // - Promotion
        val moves = mutableListOf<Move>()
        val player = board.players[player]
        val nextPos = board.getAdjacentSquare(position, player.forwardDirection)
        val nextPiece = board.getPiece(nextPos)
        if (nextPiece.isValid() && nextPiece.isEmpty()) {
            moves.add(SimpleMove(position, nextPos, false))
            val secondPos = board.getAdjacentSquare(nextPos, player.forwardDirection)
            val secondPiece = board.getPiece(secondPos)
            if (secondPiece.isValid() && secondPiece.isEmpty()) {
                moves.add(SimpleMove(position, secondPos, false))
            }
        }
        return moves
    }
}
