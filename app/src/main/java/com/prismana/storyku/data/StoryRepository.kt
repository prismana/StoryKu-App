package com.prismana.storyku.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.prismana.storyku.data.preferences.StoryPreferences
import com.prismana.storyku.data.remote.response.ErrorResponse
import com.prismana.storyku.data.remote.response.LoginResponse
import com.prismana.storyku.data.remote.response.RegisterResponse
import com.prismana.storyku.data.remote.response.StoryResponse
import com.prismana.storyku.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import kotlin.concurrent.Volatile

class StoryRepository(
    private val apiService: ApiService,
    private val storyPreferences: StoryPreferences
) {

    fun getAllStory(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage?: "No error"))
        } catch (e: RuntimeException) {
            emit(Result.Error(e.message.toString()))
        }
    }

    // maps
    fun getStoryLocation(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage?: "No error"))
        }
    }

    fun postStory(file: MultipartBody.Part, description: RequestBody): LiveData<Result<ErrorResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postStory(file, description)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage?: "No error"))
        }
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            storyPreferences: StoryPreferences
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService, storyPreferences)
        }.also { instance = it }
    }
}