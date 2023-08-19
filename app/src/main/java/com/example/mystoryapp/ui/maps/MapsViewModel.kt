package com.example.mystoryapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.ResultState
import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.data.response.GetAllStoriesResponse

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoriesWithMaps(token: String): LiveData<ResultState<GetAllStoriesResponse>> {
        return storyRepository.getMaps(token)
    }
}