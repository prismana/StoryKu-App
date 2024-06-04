package com.prismana.storyku.story.home

import androidx.lifecycle.ViewModel
import com.prismana.storyku.data.StoryRepository

class StoryViewModel(private val repository: StoryRepository): ViewModel() {

    fun getAllStories() = repository.getAllStory()

}