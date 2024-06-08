package com.prismana.storyku.story.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.prismana.storyku.R
import com.prismana.storyku.StoryViewModelFactory
import com.prismana.storyku.UserViewModelFactory
import com.prismana.storyku.databinding.ActivityHomeStoryBinding
import com.prismana.storyku.maps.StoryMapsActivity
import com.prismana.storyku.onboarding.OnboardingActivity
import com.prismana.storyku.story.add.AddStoryActivity

class HomeStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeStoryBinding

    // view model for story api data fetching
    private val storyViewModel by viewModels<StoryViewModel> {
        StoryViewModelFactory.getInstance(this@HomeStoryActivity)
    }

    // view model for user auth
    private val userViewModel by viewModels<UserViewModel> {
        UserViewModelFactory.getInstance(this@HomeStoryActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.addStoryButton.setOnClickListener {
            startActivity(Intent(this@HomeStoryActivity, AddStoryActivity::class.java))
        }

        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuLogout -> {
                    logOut()
                    true
                }
                R.id.menuMaps -> {
                    val intent = Intent(this@HomeStoryActivity, StoryMapsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    true
                }

                else -> { false }
            }
        }

        checkUser()

    }

    // to check if user status is logged in
    private fun checkUser() {
        userViewModel.getSession().observe(this@HomeStoryActivity) { user ->
            if (!user.isLogin) {
                Log.d("CEK TOKEN DI SESSI=====", user.token.toString())
                startActivity(Intent(this@HomeStoryActivity, OnboardingActivity::class.java))
                finish()
            } else {
                Log.d("CEK TOKEN DI SESSI=====", user.token.toString())
                showMessage(user.token ?: "Token kosong")
                fetchStories()
            }
        }
    }

    // get all story from api
    private fun fetchStories() {
        val storyAdapter = HomeStoryAdapter()

        binding.rvStory.layoutManager = LinearLayoutManager(this@HomeStoryActivity)
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        storyViewModel.story.observe(this@HomeStoryActivity) { result ->
            storyAdapter.submitData(lifecycle, result)
            binding.progressIndicator.visibility = View.GONE
        }
        storyAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh is LoadState.Loading) {
                binding.progressIndicator.visibility = View.VISIBLE
            } else {
                binding.progressIndicator.visibility = View.GONE
            }
        }

    }

    private fun logOut() {
        userViewModel.removeSession()
        val intent = Intent(this@HomeStoryActivity, OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showMessage(string: String) {
        Toast.makeText(this@HomeStoryActivity, string, Toast.LENGTH_SHORT).show()
    }

}