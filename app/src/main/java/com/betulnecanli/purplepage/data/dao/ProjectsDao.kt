package com.betulnecanli.purplepage.data.dao

import androidx.room.*
import com.betulnecanli.purplepage.data.model.Projects
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectsDao {

    @Insert
    suspend fun insertProject(p : Projects)

    @Update
    suspend fun updateProject(p: Projects)

    @Delete
    suspend fun deleteProject(p: Projects)

    @Query("SELECT * FROM projectTable ORDER BY projectTitle ASC")
    fun getAllProjects(): Flow<List<Projects>>

    @Query("SELECT * FROM projectTable WHERE projectTitle LIKE '%'|| :query || '%' ORDER BY projectTitle ASC")
    fun searchProject(query : String) : Flow<List<Projects>>

}