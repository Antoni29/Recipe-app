package com.quetzoft.recipes.presentation.recipe_map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.quetzoft.recipes.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var viewModel: RecipeMapViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var position: LatLng
    private lateinit var recipeTitle: String
    private var address = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_precipe_map)
        val toolbar = findViewById<Toolbar>(R.id.mapToolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        viewModel = ViewModelProvider(this)[RecipeMapViewModel::class.java]

        intent.extras?.let {  bundle ->
            val lat = bundle.getDouble("latitude")
            val lng = bundle.getDouble("longitude")
            viewModel.getAddressFromLocation(lat, lng)
            position = LatLng(lat, lng)
            recipeTitle = bundle.getString("recipeTitle").toString()
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.address.observe(this) { address ->
            this.address = address
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        val marker = MarkerOptions()
            .position(position)
            .title("Recipe: $recipeTitle")
            .snippet("Address: $address")
        mMap.addMarker(marker)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15F))
    }
}