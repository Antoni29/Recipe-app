package com.quetzoft.recipes.data.remote.dto

import com.quetzoft.recipes.domain.model.Recipe

data class RecipeDto(
    val id: Int,
    val title: String,
    val image: String
)

fun RecipeDto.toRecipe(): Recipe {
    return Recipe(
        id = id,
        title = title,
        image = image
    )
}