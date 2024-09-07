package dev.trindadedev.tinyinstaller.data.settings.model.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec

import dev.trindadedev.tinyinstaller.data.settings.model.room.dao.AppDao
import dev.trindadedev.tinyinstaller.data.settings.model.room.dao.ConfigDao
import dev.trindadedev.tinyinstaller.data.settings.model.room.entity.AppEntity
import dev.trindadedev.tinyinstaller.data.settings.model.room.entity.ConfigEntity
import dev.trindadedev.tinyinstaller.data.settings.model.room.entity.converter.AuthorizerConverter
import dev.trindadedev.tinyinstaller.data.settings.model.room.entity.converter.InstallModeConverter

import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Database(
    entities = [AppEntity::class, ConfigEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = InstallerRoom.AUTO_MIGRATION_2_3::class),
    ]
)
@TypeConverters(
    AuthorizerConverter::class,
    InstallModeConverter::class
)
abstract class InstallerRoom : RoomDatabase() {
    @DeleteColumn(tableName = "config", columnName = "analyser")
    @DeleteColumn(tableName = "config", columnName = "compat_mode")
    class AUTO_MIGRATION_2_3 : AutoMigrationSpec

    companion object : KoinComponent {
        fun createInstance(): InstallerRoom {
            return Room.databaseBuilder(
                get(),
                InstallerRoom::class.java,
                "installer.db",
            )
                .createFromAsset("databases/installer.db")
                .build()
        }
    }

    abstract val appDao: AppDao

    abstract val configDao: ConfigDao
}