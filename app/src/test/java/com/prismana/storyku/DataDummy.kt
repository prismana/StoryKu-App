package com.prismana.storyku

import com.prismana.storyku.data.remote.response.StoryResponse

object DataDummy {

    fun generateDummyQuoteResponse(): List<StoryResponse.ListStoryItem> {
        val items: MutableList<StoryResponse.ListStoryItem> = arrayListOf()
        for (i in 1..10) {
            val story = StoryResponse.ListStoryItem(
                "Photo url $i",
                "Created at $i",
                "Nama + $i",
                "Description + $i",
                i.toDouble(),
                "id $i",
                i.toDouble()
            )
            items.add(story)
        }

        return items
    }
}