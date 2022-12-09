package dev.anarchy.common.game.pieces

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.moves.EnPassantMove
import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.moves.SimpleMove
import dev.anarchy.common.game.pieces.base.Piece

class Pawn(override val player: Int) : Piece {
    override val name = "pawn"

    override fun getValidMoves(board: Board): List<Move> {
        // TODO: More players?
        // TODO: Stop hardcoding en passant rank checks
        return if (player == 0) {
            getWhitePawnMoves(board)
        } else {
            getBlackPawnMoves(board)
        }
    }

    private fun getWhitePawnMoves(board: Board): List<Move> {
        val moves = mutableListOf<Move>()
        val idx = board.pieces.indexOf(this)
        val rank = idx / board.width
        val file = idx % board.width
        val from = Pair(file, rank)

        val next = Pair(file, rank + 1)
        val nextPiece = board.getPiece(next)
        if (nextPiece.isEmpty()) {
            moves.add(SimpleMove(from, next))

            if (rank == 1) {
                val to = Pair(file, rank + 2)
                val piece = board.getPiece(to)
                if (piece.isEmpty()) {
                    moves.add(SimpleMove(from, to))
                }
            }
        }

        // captures
        if (file > 0) {
            val to = Pair(file - 1, rank + 1)
            val piece = board.getPiece(to)
            if (!piece.isEmpty() && piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        if (file < board.width - 1) {
            val to = Pair(file + 1, rank + 1)
            val piece = board.getPiece(to)
            if (!piece.isEmpty() && piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        // En passant
        val lastMove = board.lastMove
        if (rank == 4 && lastMove != null) {
            val lFrom = lastMove.sourcePosition()
            val lTo = lastMove.targetPosition()
            if (board.getPiece(lTo) is Pawn && lFrom.second == 6 && lTo.second == 4) {
                if (lTo.first == file - 1) {
                    val to = Pair(file - 1, rank + 1)
                    moves.add(EnPassantMove(from, to))
                } else if (lTo.first == file + 1) {
                    val to = Pair(file + 1, rank + 1)
                    moves.add(EnPassantMove(from, to))
                }
            }
        }

        return moves
    }

    private fun getBlackPawnMoves(board: Board): List<Move> {
        val moves = mutableListOf<Move>()
        val idx = board.pieces.indexOf(this)
        val rank = idx / board.width
        val file = idx % board.width
        val from = Pair(file, rank)

        val next = Pair(file, rank - 1)
        val nextPiece = board.getPiece(next)
        if (nextPiece.isEmpty()) {
            moves.add(SimpleMove(from, next))

            if (rank == 6) {
                val to = Pair(file, rank - 2)
                val piece = board.getPiece(to)
                if (piece.isEmpty()) {
                    moves.add(SimpleMove(from, to))
                }
            }
        }

        // captures
        if (file > 0) {
            val to = Pair(file - 1, rank - 1)
            val piece = board.getPiece(to)
            if (!piece.isEmpty() && piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        if (file < board.width - 1) {
            val to = Pair(file + 1, rank - 1)
            val piece = board.getPiece(to)
            if (!piece.isEmpty() && piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        // En passant
        val lastMove = board.lastMove
        if (rank == 3 && lastMove != null) {
            val lFrom = lastMove.sourcePosition()
            val lTo = lastMove.targetPosition()
            if (board.getPiece(lTo) is Pawn && lFrom.second == 1 && lTo.second == 3) {
                if (lTo.first == file - 1) {
                    val to = Pair(file - 1, rank - 1)
                    moves.add(EnPassantMove(from, to))
                } else if (lTo.first == file + 1) {
                    val to = Pair(file + 1, rank - 1)
                    moves.add(EnPassantMove(from, to))
                }
            }
        }

        return moves
    }

    override fun hasPriority(): Boolean {
        return true
    }
}
