package com.percivalruiz.tawk.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Note (
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "content")
    val content: String
)