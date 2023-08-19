package com.example.mystoryapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.databinding.ActivityDetailBinding
import com.example.mystoryapp.withDateFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupAction()
    }

    private fun setupBinding() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupAction() {

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)
        binding.apply {
            tvDetailName.text = story?.name
            tvDetailDescription.text = story?.description
            tvDetailCreated.withDateFormat(story?.createdAt.toString())

        }
        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.ivDetailPhoto)
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}