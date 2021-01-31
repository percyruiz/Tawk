package com.percivalruiz.tawk.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.jraska.livedata.test
import com.percivalruiz.tawk.data.Note
import com.percivalruiz.tawk.data.UserProfile
import com.percivalruiz.tawk.repository.Repository
import com.percivalruiz.tawk.ui.profile.ProfileViewModel
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
class ProfileViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var underTest: ProfileViewModel

    @RelaxedMockK
    private lateinit var handle: SavedStateHandle

    @MockK
    private lateinit var repository: Repository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should get user profile`() = runBlockingTest {
        val profile = createUserProfile(id = 1, login = "userTest")
        val note = Note(1, "note")

        coEvery { repository.getProfile("testUser") } returns flow {
            emit(
                profile
            )
        }

        coEvery { repository.getNote(1) } returns flow {
            emit(
                note
            )
        }

        val flow = flow { emit(Pair(profile, note)) }

        coEvery {
            repository.getProfile("userTest").zip(repository.getNote(1)) { profile, note ->
                Pair(profile, note)
            }
        } returns flow


        underTest = ProfileViewModel(repository, dispatcher)
        underTest.getProfile(1, "userTest")

        flow.collect {
            Assert.assertEquals(it.first.login, profile.login)
            Assert.assertEquals(it.second.content, note.content)
        }
    }

    @Test
    fun `should be able to save note`() = runBlockingTest {
        val user = UserListViewModelTest.createUser(1, "userTest")

        every { handle.contains("search") } returns false

        coEvery { repository.getUserFromDb(1) } returns user

        coEvery { repository.saveNote(1, "note") } returns Unit

        coEvery { repository.saveUserToDb(user) } returns Unit

        underTest = ProfileViewModel(repository, dispatcher)
        underTest.saveNote(1, "note")

        underTest.noteSaveSuccess.test().assertValue(Unit)
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