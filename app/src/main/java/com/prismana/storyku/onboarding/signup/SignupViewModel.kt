package com.prismana.storyku.onboarding.signup

import androidx.lifecycle.ViewModel
import com.prismana.storyku.data.StoryRepository
import com.prismana.storyku.data.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    fun register(username: String, email: String, password: String) = repository.register(username, email, password)

}