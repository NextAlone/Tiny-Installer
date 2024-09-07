package dev.trindadedev.tinyinstaller.data.installer.model.impl.installer

import dev.trindadedev.tinyinstaller.data.installer.model.entity.ProgressEntity
import dev.trindadedev.tinyinstaller.data.installer.repo.InstallerRepo
import dev.trindadedev.tinyinstaller.data.settings.model.room.entity.ConfigEntity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProgressHandler(scope: CoroutineScope, installer: InstallerRepo) : Handler(scope, installer) {
    private var job: Job? = null

    override suspend fun onStart() {
        job = scope.launch {
            installer.progress.collect {
                when (it) {
                    is ProgressEntity.ResolvedFailed -> onResolvedFailed()
                    is ProgressEntity.ResolveSuccess -> onResolveSuccess()
                    is ProgressEntity.AnalysedSuccess -> onAnalysedSuccess()
                    else -> {}
                }
            }
        }
    }

    override suspend fun onFinish() {
        job?.cancel()
    }

    private suspend fun onResolvedFailed() {
        onResolved(false)
    }

    private suspend fun onResolveSuccess() {
        onResolved(true)
    }

    private fun onResolved(success: Boolean) {
        val installMode = installer.config.installMode
        if (installMode == ConfigEntity.InstallMode.Notification || installMode == ConfigEntity.InstallMode.AutoNotification) {
            installer.background(true)
        }
        if (success) {
            installer.analyse()
        }
    }

    private fun onAnalysedSuccess() {
        val installMode = installer.config.installMode
        if (
            installMode != ConfigEntity.InstallMode.AutoDialog
            && installMode != ConfigEntity.InstallMode.AutoNotification
        ) return
        if (installer.entities.filter { it.selected }
                .groupBy { it.app.packageName }.keys.size != 1) return
        installer.install()
    }
}