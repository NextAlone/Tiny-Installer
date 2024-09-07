package dev.trindadedev.tinyinstaller.data.app.model.impl.installer

import android.os.IBinder

import dev.trindadedev.tinyinstaller.data.recycle.util.requireShizukuPermissionGranted

import rikka.shizuku.ShizukuBinderWrapper

object ShizukuInstallerRepoImpl : IBinderInstallerRepoImpl() {
    override suspend fun iBinderWrapper(iBinder: IBinder): IBinder =
        requireShizukuPermissionGranted {
            ShizukuBinderWrapper(iBinder)
        }
}