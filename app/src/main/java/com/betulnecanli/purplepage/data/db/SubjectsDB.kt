package com.betulnecanli.purplepage.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.betulnecanli.purplepage.data.dao.SubjectsDao
import com.betulnecanli.purplepage.data.model.Subjects

@Database(entities = [Subjects::class], version = 1, exportSchema = false)
abstract class SubjectsDB: RoomDatabase() {
    abstract fun subjectsDao(): SubjectsDao
}