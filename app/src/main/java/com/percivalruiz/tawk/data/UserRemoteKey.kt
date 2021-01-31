package com.percivalruiz.tawk.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class UserRemoteKey(
    @PrimaryKey val uid: Int = 0,
    @ColumnInfo(name = "next_page_key") val nextPageKey: Int
)