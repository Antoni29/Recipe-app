package com.quetzoft.recipes.domain.model

data class RecipeDetail(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val summary: String,
    val instructions: String
)
