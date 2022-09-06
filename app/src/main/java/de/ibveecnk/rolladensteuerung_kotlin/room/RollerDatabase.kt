package de.ibveecnk.rolladensteuerung_kotlin.room

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import de.ibveecnk.rolladensteuerung_kotlin.room.daos.RollerDao
import de.ibveecnk.rolladensteuerung_kotlin.room.schemas.RollerSchema
import kotlinx.coroutines.Dispatchers

@Database(entities = [RollerSchema::class], version = 2, exportSchema = true)
abstract class RollerDatabase : RoomDatabase() {
        abstract fun rollerDao(): RollerDao

        companion object {
                private var instance: RollerDatabase? = null

                @Synchronized
                fun getInstance(ctx: Context): RollerDatabase {
                        if (instance == null) {
                                instance = Room.databaseBuilder(
                                        ctx.applicationContext, RollerDatabase::class.java,
                                        "roller_database"
                                ).fallbackToDestructiveMigration()
                                        .addCallback(roomCallback)
                                        .build()
                        }
                        return instance!!
                }

                private val roomCallback = object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Thread {
                                        instance?.rollerDao()?.insert(
                                                RollerSchema(
                                                        "long tap to delete",
                                                        "0.0.0.0"
                                                )
                                        )
                                        instance?.rollerDao()?.insert(
                                                RollerSchema(
                                                        "press + to add devices",
                                                        "0.0.0.0"
                                                )
                                        )
                                }.start()
                        }
                }
        }
}
