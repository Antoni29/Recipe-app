package com.quetzoft.recipes.domain.repository

import com.quetzoft.recipes.data.remote.dto.RecipeDetailDto
import com.quetzoft.recipes.data.remote.dto.RecipesResponseDto

interface RecipeRepository {

    suspend fun getRecipes(): RecipesResponseDto

    suspend fun getRecipeDetail(recipeId: Int): RecipeDetailDto
}