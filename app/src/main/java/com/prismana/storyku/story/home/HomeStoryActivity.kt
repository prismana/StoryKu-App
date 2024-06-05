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
import androidx.recyclerview.widget.LinearLayoutManager
import com.prismana.storyku.R
import com.prismana.storyku.StoryViewModelFactory
import com.prismana.storyku.UserViewModelFactory
import com.prismana.storyku.data.Result
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

    private val storyAdapter by lazy {
        HomeStoryAdapter(this@HomeStoryActivity)
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

        binding.rvStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = storyAdapter
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
        storyViewModel.getAllStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    Result.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.progressIndicator.visibility = View.GONE
                        showMessage(result.error)
                    }
                    is Result.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        // trying handle fatal exception main
                        try {
                            storyAdapter.setStoryData(result.data.listStory)
                        } catch (e: Exception) {
                            showMessage(e.message.toString())
                        }

                    }
                }
            }
        }
    }

    private fun showMessage(string: String) {
        Toast.makeText(this@HomeStoryActivity, string, Toast.LENGTH_SHORT).show()
    }

    private fun logOut() {
        userViewModel.removeSession()
        val intent = Intent(this@HomeStoryActivity, OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        fetchStories()
    }
}