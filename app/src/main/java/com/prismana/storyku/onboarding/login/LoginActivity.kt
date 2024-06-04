package com.prismana.storyku.onboarding.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.prismana.storyku.R
import com.prismana.storyku.UserViewModelFactory
import com.prismana.storyku.data.Result
import com.prismana.storyku.data.remote.response.LoginResponse
import com.prismana.storyku.databinding.ActivityLoginBinding
import com.prismana.storyku.story.home.HomeStoryActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginButton.setOnClickListener {
            login()
        }

    }

    private fun login() {

        val email = binding.emailEditText .text.toString()
        val password = binding.passwordEditText.text.toString()

        if (binding.emailEditText.text!!.isEmpty() || binding.passwordEditText.text!!.isEmpty()) {
            binding.apply {
                emailEditText.error = "Email tidak boleh kosong!"
                passwordEditText.error = "Password tidak boleh kosong!"

                Log.e("CHECK EDIT TEXT", "email = ${email.isEmpty()}, password: ${password.isEmpty()} \n" +
                        (email + password)
                )
            }
        } else {
            loginViewModel.login(email, password).observe(this) { result ->
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
                            showMessage(result.data.message.toString())
                            result.data.loginResult?.let {
                                saveToken(it)
                                Log.d("LOGIN SAVE SESSION TEST", it.token.toString())
                            }
                            startActivity(Intent(this@LoginActivity, HomeStoryActivity::class.java))
                        }
                    }

                }
            }

        }
    }

    private fun showMessage(string: String) {
        Toast.makeText(this@LoginActivity, string, Toast.LENGTH_SHORT).show()
    }

    private fun saveToken(user: LoginResponse.LoginResult) {
        loginViewModel.saveSession(user)
    }

}