package com.test.my_attendance.di

import android.content.Context
import androidx.room.Room
import com.test.my_attendance.data.GeofenceDatabase
import com.test.my_attendance.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        GeofenceDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: GeofenceDatabase) = database.geofenceDao()
}