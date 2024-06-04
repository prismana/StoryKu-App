package com.prismana.storyku.maps

import androidx.lifecycle.ViewModel
import com.prismana.storyku.data.StoryRepository

class StoryMapsViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getStoryLocation() = repository.getStoryLocation()
}