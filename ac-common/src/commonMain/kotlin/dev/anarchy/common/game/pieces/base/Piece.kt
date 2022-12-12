package dev.anarchy.common.game.pieces.base

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.Position
import dev.anarchy.common.game.moves.Move

interface Piece {
    val name: String
    val player: Int

    fun getForcedMoves(board: Board, position: Position): List<Move>
    fun getUnforcedMoves(board: Board, position: Position): List<Move>
    fun isKing(): Boolean = false

    fun isValid() = this !is INVALID
    fun isEmpty() = this is EMPTY

    object EMPTY : Piece {
        override val name = ""
        override val player = -1

        override fun getForcedMoves(board: Board, position: Position) = emptyList<Move>()
        override fun getUnforcedMoves(board: Board, position: Position) = emptyList<Move>()
    }

    object INVALID : Piece {
        override val name = "INVALID"
        override val player = -2

        override fun getForcedMoves(board: Board, position: Position) = emptyList<Move>()
        override fun getUnforcedMoves(board: Board, position: Position) = emptyList<Move>()
    }
}
