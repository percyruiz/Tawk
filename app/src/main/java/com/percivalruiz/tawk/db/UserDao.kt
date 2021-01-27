package com.percivalruiz.tawk.db

import androidx.paging.PagingSource
import androidx.room.*
import com.percivalruiz.tawk.data.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user")
    fun getAllWithPage(): PagingSource<Int, User>


    @Query("SELECT * FROM user where login LIKE '%' || :search || '%'")
    fun getSearchWithPage(search: String): PagingSource<Int, User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg user: User)

    @Query("DELETE FROM user")
    fun nukeUser()
}