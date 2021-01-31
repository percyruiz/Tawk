package com.percivalruiz.tawk.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.jraska.livedata.test
import com.percivalruiz.tawk.data.GithubService
import com.percivalruiz.tawk.data.Note
import com.percivalruiz.tawk.data.UserProfile
import com.percivalruiz.tawk.db.AppDatabase
import com.percivalruiz.tawk.repository.Repository
import com.percivalruiz.tawk.ui.profile.ProfileViewModel
import com.percivalruiz.tawk.viewmodel.UserListViewModelTest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@ExperimentalCoroutinesApi
class RepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var underTest: RepositoryImpl

    @RelaxedMockK
    private lateinit var handle: SavedStateHandle

    @MockK
    private lateinit var db: AppDatabase

    @MockK
    private lateinit var service: GithubService

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)

        underTest = RepositoryImpl(service, db)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should save user to db`() = runBlockingTest {
        val user = UserListViewModelTest.createUser(1, "userTest")
        coEvery { db.userDao().insert(user) } returns Unit


    }



    private fun createUserProfile(id: Long, login: String) = UserProfile(
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
        name = "",
        followers = 100,
        following = 100,
        createdAt = "",
        updatedAt = "",
        location = null
    )
}