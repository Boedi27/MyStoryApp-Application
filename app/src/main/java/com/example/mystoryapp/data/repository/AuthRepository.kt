package com.example.mystoryapp.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.mystoryapp.data.ResultState
import com.example.mystoryapp.data.response.LoginResponse
import com.example.mystoryapp.data.response.RegisterResponse
import com.example.mystoryapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository private constructor(
    private val dataStore: DataStore<Preferences>,
    private val apiService: ApiService
) {

    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.login(email, password)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            Log.d("AuthRepository", "Login : ${e.message.toString()}")
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun register(name: String, email: String, password: String) : LiveData<ResultState<RegisterResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.register(name, email, password)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            Log.d("AuthRepository", "Register: ${e.message.toString()} ")
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun setToken(token: String, isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[STATE_KEY] = isLogin
        }
    }

    fun isLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[STATE_KEY] = false
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: AuthRepository? = null

        private val TOKEN = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>, apiService: ApiService): AuthRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthRepository(dataStore, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}