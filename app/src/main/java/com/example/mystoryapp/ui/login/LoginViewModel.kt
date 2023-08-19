package com.example.mystoryapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.ResultState
import com.example.mystoryapp.data.repository.AuthRepository
import com.example.mystoryapp.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun setToken(token: String, isLogin: Boolean) {
        viewModelScope.launch {
            authRepository.setToken(token, isLogin)
        }
    }

    fun getToken(): LiveData<String> {
        return authRepository.getToken().asLiveData()
    }

    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> {
        return authRepository.login(email, password)
    }
}
