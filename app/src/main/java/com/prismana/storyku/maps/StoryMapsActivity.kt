package com.prismana.storyku.maps

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.prismana.storyku.R
import com.prismana.storyku.StoryViewModelFactory
import com.prismana.storyku.data.Result
import com.prismana.storyku.data.remote.response.StoryResponse
import com.prismana.storyku.databinding.ActivityStoryMapsBinding

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding

    private val storyMapViewModel by viewModels<StoryMapsViewModel> {
        StoryViewModelFactory.getInstance(this@StoryMapsActivity)
    }

    private var storyLocation = emptyArray<StoryResponse.ListStoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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
                        //
                    }
                }
            }
        }
    }

    private fun allMarker(locations: List<StoryResponse.ListStoryItem>) {
        locations.forEach { data ->
            val latLng = LatLng(data.lat!!, data.lon!!)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(data.name)
                    .snippet(data.description)
            )

        }
    }

    private fun showToast(string: String) {
        Toast.makeText(this@StoryMapsActivity, string, Toast.LENGTH_SHORT).show()
    }

}