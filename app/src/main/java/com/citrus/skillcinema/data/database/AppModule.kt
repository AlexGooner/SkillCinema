package com.citrus.skillcinema.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        AppDataBase::class.java,
        "db"
    )
        .build()

    @Singleton
    @Provides
    fun provideSavedFilmDao(db: AppDataBase) = db.savedFilmDao()

}