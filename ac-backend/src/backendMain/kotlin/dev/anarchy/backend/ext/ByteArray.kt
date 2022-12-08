package dev.anarchy.backend.ext

fun ByteArray.hex(): String {
    return joinToString("") { "%02x".format(it) }
}
