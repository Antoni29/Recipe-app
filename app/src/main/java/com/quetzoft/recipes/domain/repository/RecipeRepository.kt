package com.quetzoft.recipes.domain.repository

import com.quetzoft.recipes.data.remote.dto.RecipeDetailDto
import com.quetzoft.recipes.data.remote.dto.RecipeDto

interface RecipeRepository {

    suspend fun getRecipes(): List<RecipeDto>

    suspend fun getRecipeDetail(recipeId: Int): RecipeDetailDto
}