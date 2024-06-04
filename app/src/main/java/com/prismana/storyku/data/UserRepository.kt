package com.prismana.storyku.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.prismana.storyku.data.preferences.StoryPreferences
import com.prismana.storyku.data.remote.response.ErrorResponse
import com.prismana.storyku.data.remote.response.LoginResponse
import com.prismana.storyku.data.remote.response.RegisterResponse
import com.prismana.storyku.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository(
    private val apiService: ApiService,
    private val storyPreferences: StoryPreferences
) {

    // register user
    fun register(username: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(username, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage?: "No error"))
        }

    }

    // login user
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage?: "No error"))
        }

    }

    // save user information to datastore
    suspend fun saveSession(user: LoginResponse.LoginResult) {
        storyPreferences.saveSession(user)
    }

    // delete user information from datastore
    suspend fun removeSession() {
        storyPreferences.removeSession()
    }

    // get user information from datastore
    fun getSession(): Flow<LoginResponse.LoginResult> {
        return storyPreferences.getSession()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            storyPreferences: StoryPreferences
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, storyPreferences)
        }.also { instance = it }
    }


}