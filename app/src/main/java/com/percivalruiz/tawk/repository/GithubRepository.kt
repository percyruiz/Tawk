package com.percivalruiz.tawk.repository

import androidx.paging.PagingData
import com.percivalruiz.tawk.data.User
import kotlinx.coroutines.flow.Flow

interface GithubRepository {

    suspend fun getUsers(since: Int = 0): Flow<List<User>>

    suspend fun getUsers(since: Int = 0, search: String = ""): Flow<PagingData<User>>
}