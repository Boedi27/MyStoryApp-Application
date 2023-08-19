package com.example.mystoryapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapp.data.ResultState
import com.example.mystoryapp.data.repository.AuthRepository
import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.data.response.AddStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val authRepository: AuthRepository, private val storyRepository: StoryRepository) : ViewModel() {
    fun getToken() : LiveData<String> {
        return authRepository.getToken().asLiveData()
    }

    fun addStory(token: String, photo: MultipartBody.Part, desc: RequestBody): LiveData<ResultState<AddStoryResponse>> {
       return storyRepository.addStories(token, photo, desc)
    }
}