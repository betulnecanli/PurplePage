package com.betulnecanli.purplepage.di

import android.content.Context
import androidx.room.Room
import com.betulnecanli.purplepage.db.SubjectsDB
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
    fun provideSubjectDb(
        @ApplicationContext context: Context,
        )= Room.databaseBuilder(
        context,
        SubjectsDB::class.java,
        "subjects_db"
        ).build()


    @Singleton
    @Provides
    fun provideSubjectDao(
        db: SubjectsDB
    ) = db.subjectsDao()
}