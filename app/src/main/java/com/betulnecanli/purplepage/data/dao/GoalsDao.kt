package com.betulnecanli.purplepage.data.dao

import androidx.room.*
import com.betulnecanli.purplepage.data.model.Goals
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(g : Goals)

    @Delete
    suspend fun deleteGoal(g : Goals)

    @Update
    suspend fun updateGoal(g : Goals)

    @Query("SELECT * FROM goalsTable WHERE goalTitle LIKE '%' || :query || '%' ORDER BY goalTitle ASC")
    fun searchGoals(query :String) : Flow<List<Goals>>

    @Query("SELECT * FROM goalsTable ORDER BY goalTitle ASC")
    fun getAllGoals() : Flow<List<Goals>>

}