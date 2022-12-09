package dev.anarchy.common.game.pieces.base

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.moves.SimpleMove

interface KnightLikePiece : Piece {
    fun getKnightMoves(board: Board): List<Move> {
        val moves = mutableListOf<Move>()
        val idx = board.pieces.indexOf(this)
        val file = idx % board.width
        val rank = idx / board.width
        val from = Pair(file, rank)

        if (file > 0 && rank > 1) {
            val to = Pair(file - 1, rank - 2)
            val piece = board.getPiece(to)
            if (piece.isEmpty() || piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        if (file > 1 && rank > 0) {
            val to = Pair(file - 2, rank - 1)
            val piece = board.getPiece(to)
            if (piece.isEmpty() || piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        if (file > 1 && rank < board.height - 1) {
            val to = Pair(file - 2, rank + 1)
            val piece = board.getPiece(to)
            if (piece.isEmpty() || piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        if (file > 0 && rank < board.height - 2) {
            val to = Pair(file - 1, rank + 2)
            val piece = board.getPiece(to)
            if (piece.isEmpty() || piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        if (file < board.width - 1 && rank < board.height - 2) {
            val to = Pair(file + 1, rank + 2)
            val piece = board.getPiece(to)
            if (piece.isEmpty() || piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        if (file < board.width - 2 && rank < board.height - 1) {
            val to = Pair(file + 2, rank + 1)
            val piece = board.getPiece(to)
            if (piece.isEmpty() || piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        if (file < board.width - 2 && rank > 0) {
            val to = Pair(file + 2, rank - 1)
            val piece = board.getPiece(to)
            if (piece.isEmpty() || piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        if (file < board.width - 1 && rank > 1) {
            val to = Pair(file + 1, rank - 2)
            val piece = board.getPiece(to)
            if (piece.isEmpty() || piece.player != player) {
                moves.add(SimpleMove(from, to))
            }
        }

        return moves
    }
}
