package dev.anarchy.common.game

import dev.anarchy.common.game.pieces.Piece
import dev.anarchy.common.game.rules.Rule

typealias Position = Pair<Int, Int>

class Board(val width: Int, val height: Int, val rules: List<Rule>) {
    val pieces = Array<Piece>(width * height) { Piece.EMPTY }

    fun getPiece(pos: Position): Piece {
        return pieces[pos.first + pos.second * width]
    }

    fun setPiece(pos: Position, piece: Piece) {
        pieces[pos.first + pos.second * width] = piece
    }

    fun hasRule(rule: Rule): Boolean {
        return rules.contains(rule)
    }
}
