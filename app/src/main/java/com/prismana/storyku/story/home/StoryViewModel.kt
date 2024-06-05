package com.prismana.storyku.story.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.prismana.storyku.data.StoryRepository
import com.prismana.storyku.data.remote.response.StoryResponse

class StoryViewModel(repository: StoryRepository): ViewModel() {

    //fun getAllStories() = repository.getAllStory()

    val story: LiveData<PagingData<StoryResponse.ListStoryItem>> = repository.getAllStory().cachedIn(viewModelScope)

}