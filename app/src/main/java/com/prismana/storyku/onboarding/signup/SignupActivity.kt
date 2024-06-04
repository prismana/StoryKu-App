package com.prismana.storyku.onboarding.signup

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
import com.prismana.storyku.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val viewModel by viewModels<SignupViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signupButton.setOnClickListener {
            signUp()
        }

    }

    private fun showMessage(string: String) {
        Toast.makeText(this@SignupActivity, string, Toast.LENGTH_SHORT).show()
    }

    private fun signUp() {

        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (binding.nameEditText.text!!.isEmpty() || binding.emailEditText.text!!.isEmpty() || binding.passwordEditText.text!!.isEmpty()) {
            binding.apply {
                nameEditText.error = "Nama tidak boleh kosong!"
                emailEditText.error = "Email tidak boleh kosong!"
                passwordEditText.error = "Password tidak boleh kosong!"

                Log.e("CHECK EDIT TEXT", "email = ${email.isEmpty()}, username: ${name.isEmpty()}, password: ${password.isEmpty()} \n" +
                        (email+ name + password)
                )
            }
        } else {
            viewModel.register(name, email, password).observe(this) { result ->
                Log.e("CHECK EDIT TEXT", "$name, $email, $password")

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
                            showMessage(result.data.message)
                            finish()
                        }
                    }
                }

            }

            /*AlertDialog.Builder(this).apply {
                setTitle("Yeah!")
                setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                setPositiveButton("Lanjut") { _, _ ->
                    //
                }
                create()
                show()
            }*/
        }
    }

}