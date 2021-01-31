package com.percivalruiz.tawk.ui.user_list

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.percivalruiz.tawk.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest

class UserListViewModel(
    private val handle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {


    init {
        if (!handle.contains(KEY_SEARCH)) {
            handle.set(KEY_SEARCH, "")
        }
    }

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val users = handle.getLiveData<String>(KEY_SEARCH)
        .asFlow()
        .flatMapLatest { repository.getUsers(0, it) }
        .cachedIn(viewModelScope)

    fun search(search: String) {
        handle.set(KEY_SEARCH, search)
    }

    companion object {
        const val KEY_SEARCH = "search"
    }
}
