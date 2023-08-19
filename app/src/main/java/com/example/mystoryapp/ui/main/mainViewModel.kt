package com.example.mystoryapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.mystoryapp.data.ResultState
import com.example.mystoryapp.data.repository.AuthRepository
import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.data.response.GetAllStoriesResponse
import com.example.mystoryapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val authRepository: AuthRepository, private val storyRepository: StoryRepository) : ViewModel() {

    fun getToken() : LiveData<String> {
        return authRepository.getToken().asLiveData()
    }

    fun isLogin() : LiveData<Boolean> {
        return authRepository.isLogin().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun getStories(token: String) :LiveData<PagingData<ListStoryItem>> {
       return storyRepository.getStories(token)
    }
}