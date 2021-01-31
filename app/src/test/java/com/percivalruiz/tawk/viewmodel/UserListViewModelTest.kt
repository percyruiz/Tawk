package com.percivalruiz.tawk.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.jraska.livedata.test
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.repository.Repository
import com.percivalruiz.tawk.ui.user_list.UserListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var underTest: UserListViewModel

    @RelaxedMockK private lateinit var handle: SavedStateHandle
    @MockK private lateinit var repository: Repository

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
    fun `should get list of users`() {
        val users = listOf(
                createUser(1, "User1"),
                createUser(2, "User2"),
                createUser(3, "User3"),
            )

        every {
            handle.getLiveData<String>("search")
        } returns MutableLiveData("")

        every { handle.contains("search")} returns false

        coEvery { repository.getUsers(0, "") } returns flow {
            emit(PagingData.from(users))
        }

        underTest = UserListViewModel(handle, repository)

        underTest.users.asLiveData().test()
            .assertHasValue()
    }

    companion object {
        fun createUser(id: Long, login: String) = User(
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
}