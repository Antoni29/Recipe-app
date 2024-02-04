package com.quetzoft.recipes.data.repository

import com.quetzoft.recipes.data.remote.RecipeApi
import com.quetzoft.recipes.data.remote.dto.RecipeDetailDto
import com.quetzoft.recipes.data.remote.dto.RecipesResponseDto
import com.quetzoft.recipes.domain.repository.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(private val api: RecipeApi): RecipeRepository {
    override suspend fun getRecipes(): RecipesResponseDto {
        return api.getRecipes()
    }

    override suspend fun getRecipes(offset: Int): RecipesResponseDto {
        return api.getRecipes(offset = offset)
    }

    override suspend fun getRecipeDetail(recipeId: Int): RecipeDetailDto {
        return api.getRecipeDetail(recipeId)
    }
}