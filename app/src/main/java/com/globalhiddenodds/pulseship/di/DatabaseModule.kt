package com.globalhiddenodds.pulseship.di

import android.content.Context
import androidx.room.Room
import com.globalhiddenodds.pulseship.datasource.database.AppRoomDatabase
import com.globalhiddenodds.pulseship.repository.database.MessageDao
import com.globalhiddenodds.pulseship.repository.database.OwnerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideOwnerDao(database: AppRoomDatabase): OwnerDao {
        return database.ownerDao()
    }

    @Provides
    fun provideMessageDao(database: AppRoomDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context):
            AppRoomDatabase{
        return Room.databaseBuilder(
            appContext,
            AppRoomDatabase::class.java,
            "pulse_ship_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}