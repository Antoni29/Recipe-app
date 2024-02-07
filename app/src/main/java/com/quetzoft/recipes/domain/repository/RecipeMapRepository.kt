package com.quetzoft.recipes.domain.repository

interface RecipeMapRepository {
    fun getAddressFromLocation(lat: Double, lng: Double): String
}