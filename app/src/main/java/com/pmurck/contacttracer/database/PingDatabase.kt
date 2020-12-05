
package com.pmurck.contacttracer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pmurck.contacttracer.model.Ping

@Database(entities = [Ping::class], version = 1, exportSchema = false)
abstract class PingDatabase : RoomDatabase() {

    abstract val pingDAO: PingDAO

    companion object {

        @Volatile
        private var INSTANCE: PingDatabase? = null

        fun getInstance(context: Context): PingDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PingDatabase::class.java,
                        "ping_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
