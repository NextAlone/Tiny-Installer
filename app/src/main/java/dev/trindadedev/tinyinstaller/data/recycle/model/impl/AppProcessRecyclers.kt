package dev.trindadedev.tinyinstaller.data.recycle.model.impl

object AppProcessRecyclers {
    private val map = mutableMapOf<String, AppProcessRecycler>()

    fun get(shell: String): AppProcessRecycler {
        return map.getOrPut(shell) {
            AppProcessRecycler(shell)
        }
    }
}