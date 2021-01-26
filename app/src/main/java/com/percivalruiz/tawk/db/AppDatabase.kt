package com.percivalruiz.tawk.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.data.UserRemoteKey

@Database(
    entities = [User::class, UserRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun keyDao(): UserRemoteKeyDao
}