package com.percivalruiz.tawk.ui.user_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class UserListViewModel(
    private val handle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {


    init {
        if (!handle.contains(KEY_SEARCH)) {
            handle.set(KEY_SEARCH, "")
        }
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val users = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty<User>() },
        handle.getLiveData<String>(KEY_SEARCH)
            .asFlow()
            .flatMapLatest { repository.getUsers(0, it) }
            // cachedIn() shares the paging state across multiple consumers of posts,
            // e.g. different generations of UI across rotation config change
            .cachedIn(viewModelScope)
    ).flattenMerge(2)

    fun search(search: String) {
        clearListCh.offer(Unit)
        handle.set(KEY_SEARCH, search)
    }

    companion object {
        const val KEY_SEARCH = "search"
    }
}
