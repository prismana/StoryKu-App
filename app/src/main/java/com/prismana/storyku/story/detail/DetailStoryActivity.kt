package com.prismana.storyku.story.detail

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.prismana.storyku.R
import com.prismana.storyku.data.remote.response.StoryResponse
import com.prismana.storyku.databinding.ActivityDetailStoryBinding
import kotlin.math.E

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // asdfsadf asdf
        dataSetup()

    }

    private fun dataSetup() {

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, StoryResponse.ListStoryItem::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_STORY)
        }

        if (story != null) {
            binding.apply {
                Glide.with(detailImage.context)
                    .load(story.photoUrl)
                    .into(detailImage)

                detailTitle.text = story.name
                detailDescription.text = story.description
            }
        }



    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}