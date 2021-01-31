package com.percivalruiz.tawk.repository

import androidx.paging.PagingData
import com.percivalruiz.tawk.data.Note
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.data.UserProfile
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getUsers(since: Int = 0, search: String = ""): Flow<PagingData<User>>

    suspend fun getUserFromDb(id: Long): User

    suspend fun saveUserToDb(user: User)

    suspend fun getProfile(login: String): Flow<UserProfile>

    suspend fun saveNote(id: Long, content: String)

    suspend fun getNote(id: Long): Flow<Note?>
}