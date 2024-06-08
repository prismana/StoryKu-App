package com.prismana.storyku.story.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.prismana.storyku.R
import com.prismana.storyku.StoryViewModelFactory
import com.prismana.storyku.data.Result
import com.prismana.storyku.databinding.ActivityAddStoryBinding
import com.prismana.storyku.story.home.HomeStoryActivity
import com.prismana.storyku.utils.getImageUri
import com.prismana.storyku.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null

    private val addStoryViewModel by viewModels<AddStoryViewModel> {
        StoryViewModelFactory.getInstance(this@AddStoryActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // galery
        binding.galleryButton.setOnClickListener {
            galleryIntent()
        }

        // camera
        binding.cameraButton.setOnClickListener {
            cameraIntent()
        }

        // upload story
        binding.uploadAddStory.setOnClickListener {
            uploadStory()
        }

    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            binding.storyImage.setImageURI(currentImageUri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun galleryIntent() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            binding.storyImage.setImageURI(currentImageUri)
        }
        // ...
    }

    private fun cameraIntent() {
        currentImageUri = getImageUri(this@AddStoryActivity)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun uploadStory() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this@AddStoryActivity)
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.descriptionEditText.text.toString()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipart = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            addStoryViewModel.postStory(multipart, requestBody).observe(this@AddStoryActivity) { result ->
                if (result != null) {
                    when (result) {
                        Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            showToast(result.error)
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            result.data.message?.let { showToast(it) }

                            val toHomeIntent = Intent(this@AddStoryActivity, HomeStoryActivity::class.java)
                            toHomeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(toHomeIntent)
                            finish()
                        }
                    }
                }

            }

        } ?: showToast("Gambar Kosong")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}