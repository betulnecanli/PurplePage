package com.betulnecanli.purplepage.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.betulnecanli.purplepage.data.dao.ProjectsDao
import com.betulnecanli.purplepage.data.model.Projects

@Database(entities = [Projects::class], version = 1, exportSchema = false)
abstract class ProjectsDB : RoomDatabase() {
    abstract fun projectDao(): ProjectsDao
}