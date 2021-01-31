package com.percivalruiz.tawk.db

import androidx.room.*
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.data.UserRemoteKey

@Dao
interface UserRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(key: UserRemoteKey)

    @Query("SELECT * FROM remote_keys LIMIT 1")
    fun peek(): UserRemoteKey?

    @Query("DELETE FROM remote_keys")
    fun nukeKey()
}