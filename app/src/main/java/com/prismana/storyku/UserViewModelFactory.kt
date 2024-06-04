package com.prismana.storyku

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prismana.storyku.data.UserRepository
import com.prismana.storyku.di.Injection
import com.prismana.storyku.onboarding.login.LoginViewModel
import com.prismana.storyku.onboarding.signup.SignupViewModel
import com.prismana.storyku.story.home.UserViewModel

class UserViewModelFactory(private val repository: UserRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: UserViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): UserViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UserViewModelFactory(Injection.provideUserRepository(context))
            }.also { instance = it }
    }
}