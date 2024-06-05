package com.prismana.storyku.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.prismana.storyku.data.remote.response.ErrorResponse
import com.prismana.storyku.data.remote.response.StoryResponse
import com.prismana.storyku.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository(
    private val apiService: ApiService
) {

    /*fun getAllStory(): LiveData<Result<StoryResponse>> = liveData {
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
    }*/

    fun getAllStory(): LiveData<PagingData<StoryResponse.ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
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
        /** Writing this code will create new instance and update object inside.
         Error bisa terjadi karena anda menerapkan singleton object ketika memanggil fungsi getInstance pada viewmodelfactory dan repository. **/
        fun getInstance(
            apiService: ApiService
        ): StoryRepository = StoryRepository(apiService)

    }
}