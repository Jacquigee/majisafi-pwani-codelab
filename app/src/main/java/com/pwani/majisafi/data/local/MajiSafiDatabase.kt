package com.pwani.majisafi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pwani.majisafi.data.local.dao.BoreholeDao
import com.pwani.majisafi.data.local.entity.BoreholeEntity
import com.pwani.majisafi.data.local.entity.SalinityReportEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [BoreholeEntity::class, SalinityReportEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MajiSafiDatabase : RoomDatabase() {

    abstract fun boreholeDao(): BoreholeDao

    companion object {
        private const val DB_NAME = "majisafi_pwani.db"

        @Volatile
        private var INSTANCE: MajiSafiDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): MajiSafiDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, scope).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context, scope: CoroutineScope): MajiSafiDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MajiSafiDatabase::class.java,
                DB_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    scope.launch(Dispatchers.IO) {
                        INSTANCE?.boreholeDao()?.insertBoreholes(MombasaSeedData.getInitialBoreholes())
                    }
                }
            }).build()
        }
    }
}
