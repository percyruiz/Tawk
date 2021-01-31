package com.percivalruiz.tawk.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.percivalruiz.tawk.data.GithubService
import com.percivalruiz.tawk.data.Note
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.data.UserProfile
import com.percivalruiz.tawk.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryImpl(
    private val service: GithubService,
    private val db: AppDatabase
) : Repository {

    /**
     * RemoteMediator class used for Paging
     *
     * Returns [Flow<PagingData>] object the [PagingSource] produced by querying to db
     * [GithubService] requests data then inserts them to db using [GithubRemoteMediator]
     */
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getUsers(since: Int, search: String) = Pager(
        config = PagingConfig(30),
        remoteMediator = GithubRemoteMediator(db, service, search)
    ) {
        if (search.isBlank()) {
            db.userDao().getAllWithPage()
        } else {
            db.userDao().getSearchWithPage(search)
        }
    }.flow

    override suspend fun getUserFromDb(id: Long): User = db.userDao().getUser(id)

    override suspend fun saveUserToDb(user: User) {
        db.userDao().insert(user)
    }

    override suspend fun getProfile(login: String): Flow<UserProfile> {
        return flow {
            val data = service.getUserProfile(login)
            emit(data)
        }
    }

    override suspend fun saveNote(id: Long, content: String) {
        db.noteDao().insert(Note(id, content))
    }

    override suspend fun getNote(id: Long) = db.noteDao().getNote(id)

}
