package com.prismana.storyku.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.prismana.storyku.data.remote.response.StoryResponse
import com.prismana.storyku.data.remote.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryResponse.ListStoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, StoryResponse.ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponse.ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(position, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}