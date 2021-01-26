package com.percivalruiz.tawk.repository

import com.percivalruiz.tawk.data.GithubService
import com.percivalruiz.tawk.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteRepository(
    private val service: GithubService
): GithubRepository {

    override suspend fun getUsers(since: Int): Flow<List<User>> {
        return flow {
            val data = service.getUserList(since)
            emit(data)
        }
    }
}
