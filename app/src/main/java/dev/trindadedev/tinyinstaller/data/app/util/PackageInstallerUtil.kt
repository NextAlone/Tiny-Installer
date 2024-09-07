package dev.trindadedev.tinyinstaller.data.app.util

import android.content.pm.PackageInstaller

import dev.trindadedev.tinyinstaller.data.reflect.repo.ReflectRepo

import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PackageInstallerUtil {
    companion object : KoinComponent {
        const val EXTRA_LEGACY_STATUS = "android.content.pm.extra.LEGACY_STATUS"

        private val installFlagsField = get<ReflectRepo>().getDeclaredField(
            PackageInstaller.SessionParams::class.java,
            "installFlags"
        )!!.also { field ->
            field.isAccessible = true
        }

        var PackageInstaller.SessionParams.installFlags: Int
            get() = installFlagsField.getInt(this)
            set(value) {
                installFlagsField.setInt(this, value)
            }
    }
}