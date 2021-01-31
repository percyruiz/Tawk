package com.percivalruiz.tawk.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.percivalruiz.tawk.data.Note
import com.percivalruiz.tawk.data.UserProfile
import com.percivalruiz.tawk.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: Repository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _profile = MutableLiveData<Pair<UserProfile, Note?>>()
    var profile: LiveData<Pair<UserProfile, Note?>> = _profile

    private val _noteSaveSuccess = MutableLiveData<Unit>()
    val noteSaveSuccess: LiveData<Unit> = _noteSaveSuccess

    /**
     * Gets [UserProfile] from Github API and gets [Note] saved in db with the same id
     * Will fire a [LiveData] with T as Pair<UserProfile, Note?> object
     */
    fun getProfile(id: Long, login: String) {
        viewModelScope.launch(dispatcher) {
            try {
                repository.getProfile(login).zip(repository.getNote(id)) { profile, note ->
                    Pair(profile, note)
                }.collect {
                    _profile.postValue(it)
                }
            } catch (e: Throwable) {
                e
            }
        }
    }

    fun saveNote(id: Long, content: String) {
        viewModelScope.launch(dispatcher) {
            try {
                // Note and User will share the same id
                repository.saveNote(id, content)

                // Update the User's note field 
                val user = repository.getUserFromDb(id)
                user.note = content
                repository.saveUserToDb(user)
                _noteSaveSuccess.postValue(Unit)
            } catch (e: Throwable) {
                e
            }
        }
    }
}

