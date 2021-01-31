package com.percivalruiz.tawk

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.percivalruiz.tawk.data.Note
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.data.UserRemoteKey
import com.percivalruiz.tawk.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        db.close()
    }

    @Test
    fun insertAndGetUser() = runBlockingTest {
        val user = createUser(0, "user1")
        db.userDao().insert(user)
        val userDb = async {
            db.userDao().getUser(0)
        }

        userDb.await().let {
            Assert.assertEquals(user.login, it.login)
        }
    }

    @Test
    fun insertAllAndGetAllUser() = runBlockingTest {
        val users = listOf(
            createUser(0, "user1"),
            createUser(1, "user2"),
            createUser(2, "user3")
        )
        db.userDao().insertAll(*users.toTypedArray())
        val userDb = async {
            db.userDao().getAll()
        }

        userDb.await().let {
            Assert.assertEquals(users.size, it.size)
            Assert.assertEquals(users[0].login, it[0].login)
        }
    }

    private fun createUser(id: Long, login: String, note: String? = null) = User(
        login = login,
        id = id,
        nodeID = "",
        avatarURL = "",
        gravatarID = "",
        url = "",
        htmlURL = "",
        followersURL = "",
        followingURL = "",
        gistsURL = "",
        starredURL = "",
        subscriptionsURL = "",
        organizationsURL = "",
        reposURL = "",
        eventsURL = "",
        receivedEventsURL = "",
        type = "",
        siteAdmin = false,
        note = null
    )
}
