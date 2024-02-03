package com.quetzoft.recipes.data.remote.dto

import com.quetzoft.recipes.domain.model.RecipeDetail

data class RecipeDetailDto(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    val healthScore: Int,
    val readyInMinutes: Int,
    val summary: String,
    val instructions: String,
    val extendedIngredients: List<IngredientDto>
)

fun RecipeDetailDto.toRecipeDetail(): RecipeDetail {
    return RecipeDetail(
        id = id,
        title = title,
        image = image,
        imageType = imageType,
        healthScore = healthScore,
        readyInMinutes = readyInMinutes,
        summary = summary,
        instructions = instructions,
        extendedIngredients = extendedIngredients.map { it.toIngredient() }
    )
}
