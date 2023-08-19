package com.example.mystoryapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.ResultState
import com.example.mystoryapp.data.repository.AuthRepository
import com.example.mystoryapp.data.response.RegisterResponse

class RegisterViewModel(private val authRepository: AuthRepository) :ViewModel(){
    fun register(name: String, email: String, password: String): LiveData<ResultState<RegisterResponse>> {
        return authRepository.register(name, email, password)
    }
}