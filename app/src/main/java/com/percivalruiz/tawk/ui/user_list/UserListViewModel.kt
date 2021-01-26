package com.percivalruiz.tawk.ui.user_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.repository.GithubRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserListViewModel(
    private val handle: SavedStateHandle,
    private val repository: GithubRepository
) : ViewModel() {

    private val _userListFlow = MutableSharedFlow<List<User>>(replay = 0)
    val userListFlow: SharedFlow<List<User>> = _userListFlow

    fun getUsers(){
        viewModelScope.launch {
            repository.getUsers().collectLatest { users ->
                _userListFlow.emit(users)
            }
        }
    }
}