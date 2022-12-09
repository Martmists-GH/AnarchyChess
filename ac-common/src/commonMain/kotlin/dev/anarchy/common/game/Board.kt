package dev.anarchy.common.game

import dev.anarchy.common.game.moves.Move
import dev.anarchy.common.game.pieces.base.Piece
import dev.anarchy.common.game.rules.Rule

typealias Position = Pair<Int, Int>

class Board(val width: Int, val height: Int, val numPlayers: Int, val rules: List<Rule>) {
    var currentTurn: Int = 0
    val pieces = Array<Piece>(width * height) { Piece.EMPTY }
    var lastMove: Move? = null

    fun getPiece(pos: Position): Piece {
        return pieces[pos.first + pos.second * width]
    }

    fun setPiece(pos: Position, piece: Piece) {
        pieces[pos.first + pos.second * width] = piece
    }

    fun hasRule(rule: Rule): Boolean {
        return rules.contains(rule)
    }

    fun execute(move: Move) {
        move.execute(this)
        lastMove = move
        currentTurn++
        currentTurn %= numPlayers
    }

    fun getMoves(): List<Move> {
        return pieces.filter { it.player == currentTurn }.flatMap { piece ->
            piece.getValidMoves(this)
        }
    }
}
