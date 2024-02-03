package com.quetzoft.recipes.domain.model

data class RecipeDetail(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    val healthScore: Int,
    val readyInMinutes: Int,
    val summary: String,
    val instructions: String,
    val extendedIngredients: List<Ingredient>
)
