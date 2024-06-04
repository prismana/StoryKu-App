package com.prismana.storyku.story.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.prismana.storyku.data.UserRepository
import com.prismana.storyku.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

class UserViewModel (private val repository: UserRepository): ViewModel() {

    fun getSession(): LiveData<LoginResponse.LoginResult> {
        return repository.getSession().asLiveData()
    }

    fun removeSession() {
        viewModelScope.launch {
            repository.removeSession()
        }
    }
}