package com.percivalruiz.tawk.repository

import android.util.Log
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

    override suspend fun getUsers(since: Int): Flow<List<User>> {
        return flow {
            val data = service.getUserList(since)
            emit(data)

            db.userDao().insertAll(*data.toTypedArray())
        }
    }

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

    override suspend fun getProfile(login: String): Flow<UserProfile> {
        return flow {
            val data = service.getUserProfile(login)
            Log.d("test", data.name)
            emit(data)
        }
    }

    override suspend fun saveNote(id: Long, content: String) {
        db.noteDao().insert(Note(id, content))
    }

    override suspend fun getNote(id: Long) = db.noteDao().getNote(id)

}
