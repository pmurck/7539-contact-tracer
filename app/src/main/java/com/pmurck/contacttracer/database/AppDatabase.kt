
package com.pmurck.contacttracer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pmurck.contacttracer.model.Contact
import com.pmurck.contacttracer.model.Ping
import com.pmurck.contacttracer.model.Stay

@Database(entities = [Ping::class, Stay::class, Contact::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val pingDAO: PingDAO
    abstract val stayDAO: StayDAO
    abstract val contactDAO: ContactDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
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
