package com.prismana.storyku.story.add

import androidx.lifecycle.ViewModel
import com.prismana.storyku.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository): ViewModel() {

    fun postStory(file: MultipartBody.Part, description: RequestBody) = repository.postStory(file, description)
}