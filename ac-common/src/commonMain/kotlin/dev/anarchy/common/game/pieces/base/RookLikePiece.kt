package dev.anarchy.common.game.pieces.base

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.moves.SimpleMove

interface RookLikePiece : Piece {
    fun getFileMoves(board: Board): List<Move> {
        val moves = mutableListOf<Move>()
        val idx = board.pieces.indexOf(this)
        val file = idx % board.width
        val rank = idx / board.width
        val from = Pair(file, rank)

        if (file > 0) {
            for (i in file - 1 downTo 0) {
                val to = Pair(i, rank)
                val piece = board.getPiece(to)
                if (piece.isEmpty()) {
                    moves.add(SimpleMove(from, to))
                } else {
                    if (piece.player != player) {
                        moves.add(SimpleMove(from, to))
                    }
                    break
                }
            }
        }

        if (file < board.width-1) {
            for (i in file + 1 until board.width) {
                val to = Pair(i, rank)
                val piece = board.getPiece(to)
                if (piece.isEmpty()) {
                    moves.add(SimpleMove(from, to))
                } else {
                    if (piece.player != player) {
                        moves.add(SimpleMove(from, to))
                    }
                    break
                }
            }
        }

        return moves
    }

    fun getRankMoves(board: Board): List<Move> {
        val moves = mutableListOf<Move>()
        val idx = board.pieces.indexOf(this)
        val file = idx % board.width
        val rank = idx / board.width
        val from = Pair(file, rank)

        if (rank > 0) {
            for (i in rank - 1 downTo 0) {
                val to = Pair(file, i)
                val piece = board.getPiece(to)
                if (piece.isEmpty()) {
                    moves.add(SimpleMove(from, to))
                } else {
                    if (piece.player != player) {
                        moves.add(SimpleMove(from, to))
                    }
                    break
                }
            }
        }

        if (rank < board.height-1) {
            for (i in rank + 1 until board.height) {
                val to = Pair(file, i)
                val piece = board.getPiece(to)
                if (piece.isEmpty()) {
                    moves.add(SimpleMove(from, to))
                } else {
                    if (piece.player != player) {
                        moves.add(SimpleMove(from, to))
                    }
                    break
                }
            }
        }

        return moves
    }
}
