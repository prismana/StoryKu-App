package com.prismana.storyku.maps

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.prismana.storyku.R
import com.prismana.storyku.StoryViewModelFactory
import com.prismana.storyku.data.Result
import com.prismana.storyku.data.remote.response.StoryResponse
import com.prismana.storyku.databinding.ActivityStoryMapsBinding
import com.prismana.storyku.story.home.HomeStoryActivity

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding

    private val storyMapViewModel by viewModels<StoryMapsViewModel> {
        StoryViewModelFactory.getInstance(this@StoryMapsActivity)
    }

    // for bound in showing all marker
    private val boundsBuilder = LatLngBounds.Builder()

    data class BoundPlace(
        val latitude: Double,
        val longitude: Double
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        getStoryLocation()
    }

    private fun getStoryLocation() {
        storyMapViewModel.getStoryLocation().observe(this@StoryMapsActivity) { result ->
            if (result != null) {
                when (result) {
                    Result.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.progressIndicator.visibility = View.GONE
                        showToast(result.error)
                    }
                    is Result.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        getAllMarker(result.data.listStory)
                        showToast("Successful get mark")
                    }
                }
            }
        }
    }

    private fun getAllMarker(locations: List<StoryResponse.ListStoryItem>) {
        locations.forEach { data ->
            val storyLatLng = LatLng(data.lat!!, data.lon!!)
            mMap.addMarker(
                MarkerOptions()
                    .position(storyLatLng)
                    .title(data.name)
                    .snippet(data.description)
            )

        }

        // make new data of sabang and merauke
        val boundPlaceList = listOf(
            BoundPlace(5.905995, 95.216975),
            BoundPlace(-9.126903, 141.019562)
        )

        // to avoid camera map move to other location than Indonesia (because there also other country)
        boundPlaceList.forEach{ boundPlace ->
            val newBoundPlace = LatLng(boundPlace.latitude, boundPlace.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newBoundPlace))
            boundsBuilder.include(newBoundPlace)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )

    }

    private fun showToast(string: String) {
        Toast.makeText(this@StoryMapsActivity, string, Toast.LENGTH_SHORT).show()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        val toHomeIntent = Intent(this@StoryMapsActivity, HomeStoryActivity::class.java)
        toHomeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(toHomeIntent)
        finish()
    }


}