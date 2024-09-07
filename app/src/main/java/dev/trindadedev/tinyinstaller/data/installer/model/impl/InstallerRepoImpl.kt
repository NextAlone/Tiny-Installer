package dev.trindadedev.tinyinstaller.data.installer.model.impl

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import dev.trindadedev.tinyinstaller.data.app.model.entity.DataEntity
import dev.trindadedev.tinyinstaller.data.installer.model.entity.ProgressEntity
import dev.trindadedev.tinyinstaller.data.installer.model.entity.SelectInstallEntity
import dev.trindadedev.tinyinstaller.data.installer.repo.InstallerRepo
import dev.trindadedev.tinyinstaller.data.settings.model.room.entity.ConfigEntity

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

import java.util.UUID
import kotlin.collections.set

class InstallerRepoImpl private constructor() : InstallerRepo, KoinComponent {
    companion object : KoinComponent {
        private val impls = mutableMapOf<String, InstallerRepoImpl>()

        private val context by inject<Context>()

        fun getOrCreate(id: String? = null): InstallerRepo {
            if (id == null) return create()
            return get(id) ?: create()
        }

        fun get(id: String): InstallerRepo? {
            return impls[id]
        }

        fun create(): InstallerRepo {
            synchronized(this) {
                val impl = InstallerRepoImpl()
                impls[impl.id] = impl
                val intent = Intent(InstallerService.Action.Ready.value)
                intent.component = ComponentName(context, InstallerService::class.java)
                intent.putExtra(InstallerService.EXTRA_ID, impl.id)
                context.startService(intent)
                return impl
            }
        }

        fun remove(id: String) {
            synchronized(this) {
                impls.remove(id)
            }
        }
    }

    override val id: String = UUID.randomUUID().toString()

    override var error: Throwable = Throwable()

    override var config: ConfigEntity = ConfigEntity.default

    override var data: List<DataEntity> by mutableStateOf(emptyList())

    override var entities: List<SelectInstallEntity> by mutableStateOf(emptyList())

    override val progress: MutableSharedFlow<ProgressEntity> =
        MutableStateFlow(ProgressEntity.Ready)

    val action: MutableSharedFlow<Action> =
        MutableSharedFlow(replay = 1, extraBufferCapacity = 1)

    override val background: MutableSharedFlow<Boolean> =
        MutableStateFlow(false)

    override fun resolve(activity: Activity) {
        action.tryEmit(Action.Resolve(activity))
    }

    override fun analyse() {
        action.tryEmit(Action.Analyse)
    }

    override fun install() {
        action.tryEmit(Action.Install)
    }

    override fun background(value: Boolean) {
        background.tryEmit(value)
    }

    override fun close() {
        action.tryEmit(Action.Finish)
    }

    sealed class Action {
        data class Resolve(val activity: Activity) : Action()

        object Analyse : Action()

        object Install : Action()

        object Finish : Action()
    }
}