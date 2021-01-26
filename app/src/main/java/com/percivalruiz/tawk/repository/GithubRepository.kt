package com.percivalruiz.tawk.repository

import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.data.UserList
import kotlinx.coroutines.flow.Flow

interface GithubRepository {

    suspend fun getUsers(since: Int = 0): Flow<List<User>>
}