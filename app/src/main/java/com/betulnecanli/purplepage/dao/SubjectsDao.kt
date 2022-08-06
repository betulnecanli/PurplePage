package com.betulnecanli.purplepage.dao

import androidx.room.*
import com.betulnecanli.purplepage.model.Subjects
import kotlinx.coroutines.flow.Flow


@Dao
interface SubjectsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject : Subjects)

    @Update
    suspend fun updateSubject(subject: Subjects)

    @Delete
    suspend fun deleteSubject(subject : Subjects)

    @Query("SELECT * FROM subjectsTable ORDER BY subjectTitle ASC")
    fun getAllSubjects(): Flow<List<Subjects>>
}