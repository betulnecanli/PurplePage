package com.betulnecanli.purplepage.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "subjectsTable")
@Parcelize
data class Subjects(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val subjectTitle : String,
    val isChecked : Boolean = false
    ):Parcelable
