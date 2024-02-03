package com.quetzoft.recipes.data.remote.dto

import com.quetzoft.recipes.domain.model.Ingredient

data class IngredientDto(
    val image: String,
    val original: String
)

fun IngredientDto.toIngredient(): Ingredient {
    return Ingredient(
        image = image,
        original = original
    )
}
