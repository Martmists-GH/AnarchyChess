package dev.anarchy.common.game.pieces.base

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.moves.Move

interface Piece {
    // 0 = white, 1 = black, etc.
    // -1 = empty
    val player: Int

    // name in snake_case, e.g. "pawn" or "knook"
    // Used for rendering the pieces
    val name: String

    // Returns a list of all possible moves for this piece
    fun getValidMoves(board: Board): List<Move>

    // If this piece has priority behavior, e.g. forced moves
    fun hasPriority(): Boolean

    fun isEmpty(): Boolean {
        return player == -1
    }

    // Piece representing an empty square
    companion object EMPTY : Piece {
        override val player: Int = -1
        override val name = "_blank"

        override fun getValidMoves(board: Board): List<Move> {
            return emptyList()
        }

        override fun hasPriority(): Boolean {
            return false
        }
    }
}
