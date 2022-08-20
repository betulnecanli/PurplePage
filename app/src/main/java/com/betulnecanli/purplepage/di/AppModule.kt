package com.betulnecanli.purplepage.di

import android.content.Context
import androidx.room.Room
import com.betulnecanli.purplepage.data.db.GoalsDB
import com.betulnecanli.purplepage.data.db.ProjectsDB
import com.betulnecanli.purplepage.data.db.SubjectsDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
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


    @Singleton
    @Provides
    fun provideProjectDb(
        @ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ProjectsDB::class.java,
        "projects_db"
        ).build()

    @Singleton
    @Provides
    fun provideProjectDao(
        db : ProjectsDB
    ) = db.projectDao()


    @Singleton
    @Provides
    fun provideGoalDb(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        GoalsDB::class.java,
        "goals_db"
    ).build()

    @Singleton
    @Provides
    fun provideGoalDao(
        db : GoalsDB
    ) = db.goalDao()


    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
 }

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope