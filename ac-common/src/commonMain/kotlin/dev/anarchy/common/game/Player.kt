package dev.anarchy.common.game

class Player(val number: Int) {
    var isInCheck = false
    var forwardDirection = Pair(0, 0)
}
