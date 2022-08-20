package com.betulnecanli.purplepage.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "projectTable")
@Parcelize
data class Projects(
    @PrimaryKey(autoGenerate = true) val uidP : Int = 0,
    val projectTitle : String,
    val isCheckedP : Boolean = false
):Parcelable
