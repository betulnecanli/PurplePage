package com.betulnecanli.purplepage.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjectsTable")
data class Subjects(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val subjectTitle : String,
    val isChecked : Boolean
    )
