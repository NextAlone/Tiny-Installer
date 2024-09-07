package dev.trindadedev.tinyinstaller.data.recycle.util

import android.content.ContentResolver
import android.content.Intent
import android.content.IntentFilter
import java.io.File

fun Array<out String>.delete() {
    for (path in this) {
        val file = File(path)
        if (file.exists()) file.delete()
    }
}

val InstallIntentFilter = IntentFilter().apply {
    addAction(Intent.ACTION_MAIN)
    addAction(Intent.ACTION_VIEW)
    addAction(Intent.ACTION_INSTALL_PACKAGE)
    addCategory(Intent.CATEGORY_DEFAULT)
    addDataScheme(ContentResolver.SCHEME_CONTENT)
    addDataScheme(ContentResolver.SCHEME_FILE)
    addDataType("application/vnd.android.package-archive")
}
