package com.prismana.storyku

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prismana.storyku.data.StoryRepository
import com.prismana.storyku.di.Injection
import com.prismana.storyku.maps.StoryMapsViewModel
import com.prismana.storyku.story.add.AddStoryViewModel
import com.prismana.storyku.story.home.StoryViewModel

class StoryViewModelFactory(private val repository: StoryRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(StoryMapsViewModel::class.java) -> {
                StoryMapsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: StoryViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): StoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryViewModelFactory(Injection.provideStoryRepository(context))
            }.also { instance = it }
    }
}