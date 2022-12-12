package dev.anarchy.common.game

import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.pieces.base.Piece
import dev.anarchy.common.game.rules.Rule

typealias Position = Pair<Int, Int>

class Board(private val rows: Int, private val columns: Int, private val numPlayers: Int, @PublishedApi internal val rules: List<Rule>) {
    val players = Array(numPlayers, ::Player)
    private val board = Array<Piece>(rows*columns) { Piece.EMPTY }
    private var currentPlayer = 0

    fun getPiece(position: Position): Piece {
        val (row, column) = position
        return board[row*columns + column]
    }

    fun setPiece(position: Position, piece: Piece) {
        val (row, column) = position
        board[row*columns + column] = piece
    }

    inline fun <reified R: Rule> hasRule(): Boolean {
        return rules.filterIsInstance<R>().isNotEmpty()
    }

    private fun positions() = sequence {
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                val pos = Pair(row, column)
                if (getPiece(pos).isValid()) {
                    yield(pos)
                }
            }
        }
    }

    private fun getForcedMoves(): List<Move> {
        val moves = mutableListOf<Move>()
        for (pos in positions()) {
            val piece = getPiece(pos)
            if (piece.player == currentPlayer) {
                moves.addAll(piece.getForcedMoves(this, pos))
            }
        }
        return moves
    }

    private fun getUnforcedMoves(): List<Move> {
        val moves = mutableListOf<Move>()
        for (pos in positions()) {
            val piece = getPiece(pos)
            if (piece.player == currentPlayer) {
                moves.addAll(piece.getUnforcedMoves(this, pos))
            }
        }
        return moves
    }

    fun getAdjacentSquare(pos: Position, direction: Position): Position {
        val (row, col) = pos
        val (rowDir, colDir) = direction
        return (row + rowDir) to (col + colDir)
    }

    private fun resolveChecks() {
        // Primitive implementation for now
        for (player in players) {
            player.isInCheck = false
        }
        for (pos in positions()) {
            val piece = getPiece(pos)
            if (piece.isValid() && !piece.isEmpty()) {
                for (move in piece.getForcedMoves(this, pos)) {
                    val targetPiece = getPiece(move.to)
                    if (!targetPiece.isEmpty() && targetPiece.player != piece.player && targetPiece.isKing()) {
                        players[targetPiece.player].isInCheck = true
                    }
                }
                for (move in piece.getUnforcedMoves(this, pos)) {
                    val targetPiece = getPiece(move.to)
                    if (!targetPiece.isEmpty() && targetPiece.player != piece.player && targetPiece.isKing()) {
                        players[targetPiece.player].isInCheck = true
                    }
                }
            }
        }
    }

    fun getMoves(): List<Move> {
        val moves = getForcedMoves()
        if (moves.isEmpty()) {
            return getUnforcedMoves()
        }
        return moves
    }

    fun playMove(move: Move) {
        move.execute(this)
        resolveChecks()
        currentPlayer = (currentPlayer + 1) % numPlayers
    }

    fun getPlayer() = players[currentPlayer]
}
