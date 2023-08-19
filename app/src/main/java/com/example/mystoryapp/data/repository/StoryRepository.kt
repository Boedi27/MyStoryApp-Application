package com.example.mystoryapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.mystoryapp.data.ResultState
import com.example.mystoryapp.data.StoryPagingSource
import com.example.mystoryapp.data.response.AddStoryResponse
import com.example.mystoryapp.data.response.GetAllStoriesResponse
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService) {

    fun getStories(token: String) : LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    fun addStories(token: String, photo: MultipartBody.Part, desc: RequestBody) : LiveData<ResultState<AddStoryResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.addStory("Bearer $token", photo, desc)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            Log.d("StoryRepository", "addStories: ${e.message.toString()} ")
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun getMaps(token: String) : LiveData<ResultState<GetAllStoriesResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getStories("Bearer $token", location = 1)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}