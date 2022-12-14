package dev.anarchy.app

import org.jetbrains.compose.web.renderComposable

fun initialize(rootElementId: String) {
    renderComposable(rootElementId) {
        App()
    }
}
