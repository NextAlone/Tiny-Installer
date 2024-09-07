package dev.trindadedev.tinyinstaller.ui.page.installer.dialog

private val emptyInnerParams = DialogInnerParams("empty")

data class DialogParams(
    val icon: DialogInnerParams = emptyInnerParams,
    val title: DialogInnerParams = emptyInnerParams,
    val subtitle: DialogInnerParams = emptyInnerParams,
    val text: DialogInnerParams = emptyInnerParams,
    val content: DialogInnerParams = emptyInnerParams,
    val buttons: DialogInnerParams = emptyInnerParams
)
