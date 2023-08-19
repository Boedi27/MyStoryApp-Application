package com.example.mystoryapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mystoryapp.data.repository.AuthRepository
import com.example.mystoryapp.data.retrofit.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object UserInjection {
    fun providePreferences(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(context.dataStore, apiService)
    }
}