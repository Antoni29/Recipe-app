package com.quetzoft.recipes.data.repository

import com.quetzoft.recipes.data.remote.RecipeApi
import com.quetzoft.recipes.data.remote.dto.RecipeDetailDto
import com.quetzoft.recipes.data.remote.dto.RecipeDto
import com.quetzoft.recipes.domain.repository.RecipeRepository

class RecipeRepositoryImpl constructor(private val api: RecipeApi): RecipeRepository {
    override suspend fun getRecipes(): List<RecipeDto> {
        return api.getRecipes()
    }

    override suspend fun getRecipeDetail(recipeId: Int): RecipeDetailDto {
        return api.getRecipeDetail(recipeId)
    }
}