package dev.anarchy.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    title = "Anarchy Chess",
) {
    App()
}

@Preview
@Composable
fun AppPreview() {
    App()
}
