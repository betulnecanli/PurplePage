package com.betulnecanli.purplepage.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.betulnecanli.purplepage.data.dao.GoalsDao
import com.betulnecanli.purplepage.data.model.Goals

@Database(entities = [Goals::class], version = 1, exportSchema = false)
abstract class GoalsDB: RoomDatabase() {
    abstract fun goalDao() : GoalsDao
}