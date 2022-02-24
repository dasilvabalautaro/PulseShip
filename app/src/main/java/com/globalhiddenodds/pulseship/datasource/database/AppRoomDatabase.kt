package com.globalhiddenodds.pulseship.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globalhiddenodds.pulseship.repository.database.MessageDao
import com.globalhiddenodds.pulseship.repository.database.OwnerDao

@Database(entities = [Owner::class, Message::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase: RoomDatabase() {
    abstract fun ownerDao(): OwnerDao
    abstract fun messageDao(): MessageDao

/*
    companion object{
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "pulse_ship_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
*/
}