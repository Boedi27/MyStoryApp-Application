package com.example.mystoryapp.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.adapter.ListStoryAdapter
import com.example.mystoryapp.adapter.LoadingStateAdapter
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.ui.add.AddStoryActivity
import com.example.mystoryapp.ui.login.LoginActivity
import com.example.mystoryapp.ui.maps.MapsActivity
import com.example.mystoryapp.viewModelFactory.StoryViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupViewModel()
        setupAction()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun setupViewModel() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }

        val factory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        mainViewModel.isLogin().observe(this) {
            if (!it) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        mainViewModel.getToken().observe(this) { token ->
            this.token = token
            if (token.isNotEmpty()) {
                val adapter = ListStoryAdapter()
                adapter.addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            showLoading(true)
                        }
                        is LoadState.NotLoading -> {
                            showLoading(false)
                        }
                        is LoadState.Error -> {
                            Toast.makeText(this@MainActivity, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this@MainActivity, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                binding.rvStory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                mainViewModel.getStories(token).observe(this) { result ->
                    adapter.submitData(lifecycle, result)
                }
            }
        }
    }

    private fun setupAction() {
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                mainViewModel.logout()
                return true
            }
            R.id.action_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(MapsActivity.EXTRA_TOKEN, token)
                startActivity(intent)
                return true
            }
            R.id.change_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            else -> return true
        }
    }
}
