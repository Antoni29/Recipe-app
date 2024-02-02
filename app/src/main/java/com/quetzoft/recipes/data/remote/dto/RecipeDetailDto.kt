package com.quetzoft.recipes.data.remote.dto

import com.quetzoft.recipes.domain.model.RecipeDetail

data class RecipeDetailDto(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val summary: String,
    val instructions: String
)

fun RecipeDetailDto.toRecipeDetail(): RecipeDetail {
    return RecipeDetail(
        id = id,
        title = title,
        image = image,
        readyInMinutes = readyInMinutes,
        summary = summary,
        instructions = instructions
    )
}
