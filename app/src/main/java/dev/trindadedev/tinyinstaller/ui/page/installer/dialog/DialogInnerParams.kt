package dev.trindadedev.tinyinstaller.ui.page.installer.dialog

import androidx.compose.runtime.Composable

data class DialogInnerParams(
    val id: String,
    val content: (@Composable () -> Unit)? = null
)
