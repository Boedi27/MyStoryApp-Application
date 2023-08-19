package com.example.mystoryapp.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.R
import com.example.mystoryapp.data.ResultState
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.ui.main.MainActivity
import com.example.mystoryapp.ui.register.RegisterActivity
import com.example.mystoryapp.viewModelFactory.AuthViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory: AuthViewModelFactory = AuthViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error =
                        resources.getString(R.string.input_message, "Email")
                    binding.btnLogin
                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error =
                        resources.getString(R.string.input_message, "Password")
                }
                password.length < 8 ->{
                    binding.edLoginPassword.error =
                        resources.getString(R.string.invalid_password)
                }
                else -> {
                    loginViewModel.login(email, password).observe(this) { result ->
                        if (result != null ) {
                            when (result) {
                                is ResultState.Loading -> {
                                    showLoading(true)
                                }
                                is ResultState.Success -> {
                                    showLoading(false)
                                    val data = result.data
                                    if (data.error) {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            data.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        showLoading(false)
                                        val token = data.loginResult?.token ?: ""
                                        loginViewModel.setToken(token, true)
                                    }
                                }
                                is ResultState.Error -> {
                                    showLoading(false)
                                    Toast.makeText(
                                        this,
                                        resources.getString(R.string.login_error),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
        binding.tvLoginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}
