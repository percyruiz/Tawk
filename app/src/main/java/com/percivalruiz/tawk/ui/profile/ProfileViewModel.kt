package com.percivalruiz.tawk.ui.profile

import android.util.Log
import androidx.lifecycle.*
import com.percivalruiz.tawk.data.Note
import com.percivalruiz.tawk.data.UserProfile
import com.percivalruiz.tawk.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _profile = MutableLiveData<Pair<UserProfile, Note?>>()
    val profile: LiveData<Pair<UserProfile, Note?>> = _profile

    fun getProfile(id: Long, login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getProfile(login).zip(repository.getNote(id)) { profile, note ->
                    Pair(profile, note)
                }.collect {
                    _profile.postValue(it)
                }


            } catch (e: Throwable) {
                Log.d("error", e.message)
            }
        }
    }

    fun saveNote(id: Long, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.saveNote(id, content)
                val user = repository.getUserFromDb(id)
                user.note = content
                repository.saveUserToDb(user)
            } catch (e: Throwable) {
                Log.d("error", e.message)
            }
        }
    }
}

