package com.quetzoft.recipes.data.repository

import android.location.Address
import android.location.Geocoder
import com.quetzoft.recipes.domain.repository.RecipeMapRepository
import javax.inject.Inject

class RecipeMapRepositoryImpl @Inject constructor(private val geocoder: Geocoder): RecipeMapRepository {
    override fun getAddressFromLocation(lat: Double, lng: Double): String {
        val addresses: MutableList<Address>? = geocoder.getFromLocation(lat, lng, 1)
        return addresses?.let {
            if(it.isNotEmpty()) {
                val address = "${it[0].thoroughfare} ${it[0].subThoroughfare}, ${it[0].locality}"
                address
            }
            else
                ""
        } ?: run {
            ""
        }
    }
}