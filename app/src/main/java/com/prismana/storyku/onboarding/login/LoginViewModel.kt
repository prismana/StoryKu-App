package com.prismana.storyku.onboarding.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prismana.storyku.data.StoryRepository
import com.prismana.storyku.data.UserRepository
import com.prismana.storyku.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository): ViewModel() {

    fun login(email: String, password:String) = repository.login(email, password)

    fun saveSession(user: LoginResponse.LoginResult) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

}