package com.example.mystoryapp.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.data.UserInjection
import com.example.mystoryapp.data.repository.AuthRepository
import com.example.mystoryapp.ui.login.LoginViewModel
import com.example.mystoryapp.ui.main.MainViewModel
import com.example.mystoryapp.ui.register.RegisterViewModel

class AuthViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null
        fun getInstance(context: Context):AuthViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: AuthViewModelFactory(UserInjection.providePreferences(context))
            }.also { instance = it }
    }
}