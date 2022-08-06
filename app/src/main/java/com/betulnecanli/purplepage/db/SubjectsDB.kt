package com.betulnecanli.purplepage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.betulnecanli.purplepage.dao.SubjectsDao
import com.betulnecanli.purplepage.model.Subjects

@Database(entities = [Subjects::class], version = 1, exportSchema = false)
abstract class SubjectsDB: RoomDatabase() {

    abstract fun subjectsDao(): SubjectsDao
}