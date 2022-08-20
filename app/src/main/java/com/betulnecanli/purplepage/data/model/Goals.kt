package com.betulnecanli.purplepage.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "goalsTable")
@Parcelize
data class Goals(
    @PrimaryKey(autoGenerate = true) val uidG : Int = 0,
    val goalTitle : String,
    val isCheckedG : Boolean = false
):Parcelable
