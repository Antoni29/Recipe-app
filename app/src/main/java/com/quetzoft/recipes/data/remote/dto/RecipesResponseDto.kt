package com.quetzoft.recipes.data.remote.dto

import com.quetzoft.recipes.domain.model.Recipe
import com.quetzoft.recipes.domain.model.RecipesResponse

data class RecipesResponseDto(
    val results: List<RecipeDto>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)

fun RecipesResponseDto.toRecipesResponse(): RecipesResponse {
    return RecipesResponse(
        results = results.map { it.toRecipe() },
        offset = offset,
        number = number,
        totalResults = totalResults
    )
}
