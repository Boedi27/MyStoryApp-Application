package com.example.mystoryapp.data

import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.data.retrofit.ApiConfig

object StoryInjection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService)
    }
}