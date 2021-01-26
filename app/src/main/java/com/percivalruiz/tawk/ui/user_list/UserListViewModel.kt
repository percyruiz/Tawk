package com.percivalruiz.tawk.ui.user_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserListViewModel(
    private val handle: SavedStateHandle,
    private val repository: GithubRepository
) : ViewModel() {


    init {
        if (!handle.contains(KEY_SEARCH)) {
            handle.set(KEY_SEARCH, "test")
        }
    }

    private val _userListFlow = MutableSharedFlow<List<User>>(replay = 0)
    val userListFlow: SharedFlow<List<User>> = _userListFlow

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val users = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty<User>() },
        handle.getLiveData<String>(KEY_SEARCH)
            .asFlow()
            .flatMapLatest { repository.getUsers(0, "") }
            // cachedIn() shares the paging state across multiple consumers of posts,
            // e.g. different generations of UI across rotation config change
            .cachedIn(viewModelScope)
    ).flattenMerge(2)

    fun getUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUsers().collectLatest { users ->
                _userListFlow.emit(users)
            }
        }
    }

    fun getItYow(search: String) {

        clearListCh.offer(Unit)

        handle.set(KEY_SEARCH, "")
    }

    companion object {
        const val KEY_SEARCH = "search"
    }
}