package com.example.mystoryapp.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.data.StoryInjection
import com.example.mystoryapp.data.UserInjection
import com.example.mystoryapp.data.repository.AuthRepository
import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.ui.add.StoryViewModel
import com.example.mystoryapp.ui.main.MainViewModel
import com.example.mystoryapp.ui.maps.MapsViewModel

class StoryViewModelFactory private constructor(private val authRepository: AuthRepository, private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(authRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(authRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryViewModelFactory? = null
        fun getInstance(context: Context): StoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryViewModelFactory(UserInjection.providePreferences(context), StoryInjection.provideRepository())
            }.also { instance = it }
    }
}