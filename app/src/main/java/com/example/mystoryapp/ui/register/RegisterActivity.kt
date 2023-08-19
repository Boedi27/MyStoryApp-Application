package com.example.mystoryapp.ui.register

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
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.ui.login.LoginActivity
import com.example.mystoryapp.viewModelFactory.AuthViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupBinding() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.edRegisterName.error =
                        resources.getString(R.string.input_message, "Name")
                }
                email.isEmpty() -> {
                    binding.edRegisterEmail.error =
                        resources.getString(R.string.input_message, "Email")
                }
                password.isEmpty() -> {
                    binding.edRegisterPassword.error =
                        resources.getString(R.string.input_message, "Password")
                }
                password.length < 8 ->{
                    binding.edRegisterPassword.error =
                        resources.getString(R.string.invalid_password)
                }
                else -> {
                    registerViewModel.register(name, email, password).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> {
                                    showLoading(true)
                                }
                                is ResultState.Success -> {
                                    showLoading(false)
                                    val data = result.data
                                    if (data.error) {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            data.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            resources.getString(R.string.register_success),
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                                is ResultState.Error -> {
                                    showLoading(false)
                                    Toast.makeText(
                                        this,
                                        resources.getString(R.string.register_error),
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
            startActivity(Intent(this, LoginActivity::class.java))
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
