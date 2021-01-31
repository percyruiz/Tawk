package com.percivalruiz.tawk.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.LoadType.*
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.percivalruiz.tawk.data.GithubService
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.data.UserRemoteKey
import com.percivalruiz.tawk.db.AppDatabase
import com.percivalruiz.tawk.db.NoteDao
import com.percivalruiz.tawk.db.UserDao
import com.percivalruiz.tawk.db.UserRemoteKeyDao
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val db: AppDatabase,
    private val service: GithubService,
    private val search: String
) : RemoteMediator<Int, User>() {

    private val userDao: UserDao = db.userDao()
    private val remoteKeyDao: UserRemoteKeyDao = db.keyDao()
    private val noteDao: NoteDao = db.noteDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): MediatorResult {
        try {

            val since = when (loadType) {
                REFRESH -> null
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    // Get the saved since value from db
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.peek()
                    }

                    // Return the saved since value
                    remoteKey?.nextPageKey ?: 0
                }
            }


            val data = service.getUserList(since)

            db.withTransaction {
                // Remove [User] data saved in db cache when refresh is being called
                if (loadType == REFRESH) {
                    userDao.nukeUser()
                }

                // Save the next since value to db, will use this once to get the next page using Github service
                remoteKeyDao.insert(
                    UserRemoteKey(
                        uid = 0,
                        nextPageKey = data.last().id.toInt()
                    )
                )

                // Checks if [Note] with id equals [User] id exists then saves the [Note] content to [User] note field
                val notes = noteDao.getAllNotes()
                data.forEach { user ->
                    try {
                        user.note = notes.first { it.id == user.id }.content
                    } catch (e: NoSuchElementException) {
                        user.note = null
                    }
                }

                // Cache to db
                userDao.insertAll(*data.toTypedArray())
            }

            // Set end of page if there is no more data being fetched and if search criteria is blank
            return MediatorResult.Success(endOfPaginationReached = data.isEmpty() || search.isNotBlank())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}