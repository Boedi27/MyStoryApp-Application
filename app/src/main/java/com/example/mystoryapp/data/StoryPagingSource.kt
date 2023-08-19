package com.example.mystoryapp.data


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.data.retrofit.ApiService


class StoryPagingSource(
    private val apiService: ApiService,
    private val token: String
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX

            val response = apiService.getStories("Bearer $token", page, params.loadSize)

            val responseData = response.listStory?.map {
                ListStoryItem(
                    id =it.id,
                    photoUrl = it.photoUrl,
                    createdAt = it.createdAt,
                    name = it.name,
                    description = it.description,
                    lat = it.lat,
                    lon = it.lon
                )
            }

            LoadResult.Page(
                data = responseData ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

