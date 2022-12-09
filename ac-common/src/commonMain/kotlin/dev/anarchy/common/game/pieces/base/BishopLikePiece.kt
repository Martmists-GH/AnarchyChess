package dev.anarchy.common.game.pieces.base

import dev.anarchy.common.game.Board
import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.moves.SimpleMove

interface BishopLikePiece : Piece {
    fun getDiagonalMoves(board: Board): List<Move> {
        val moves = mutableListOf<Move>()
        val idx = board.pieces.indexOf(this)
        val file = idx % board.width
        val rank = idx / board.width
        val from = Pair(file, rank)

        if (file > 0 && rank > 0) {
            for (i in file - 1 downTo 0) {
                val j = rank - (file - i)
                if (j < 0) {
                    break
                }
                val to = Pair(i, j)
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

        if (file < board.width-1 && rank < board.height-1) {
            for (i in file + 1 until board.width) {
                val j = rank + (i - file)
                if (j >= board.height) {
                    break
                }
                val to = Pair(i, j)
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

        if (file > 0 && rank < board.height-1) {
            for (i in file - 1 downTo 0) {
                val j = rank + (file - i)
                if (j >= board.height) {
                    break
                }
                val to = Pair(i, j)
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

        if (file < board.width-1 && rank > 0) {
            for (i in file + 1 until board.width) {
                val j = rank - (i - file)
                if (j < 0) {
                    break
                }
                val to = Pair(i, j)
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
