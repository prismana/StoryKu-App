package com.prismana.storyku.di

import android.content.Context
import com.prismana.storyku.data.preferences.StoryPreferences
import com.prismana.storyku.data.StoryRepository
import com.prismana.storyku.data.UserRepository
import com.prismana.storyku.data.remote.retrofit.ApiConfig
import com.prismana.storyku.data.preferences.datastore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    // inject token to story repository
    fun provideStoryRepository(context: Context): StoryRepository {
        // get token from datastore
        val preferences = StoryPreferences.getInstance(context.datastore)
        val user = runBlocking { preferences.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token ?: "null")

        return StoryRepository.getInstance(apiService, preferences)
    }

    // inject token to for user auth
    fun provideUserRepository(context: Context): UserRepository {
        // get token from datastore
        val preferences = StoryPreferences.getInstance(context.datastore)
        val user = runBlocking { preferences.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token ?: "null")

        return UserRepository.getInstance(apiService, preferences)
    }
}