package com.quetzoft.recipes.data.remote

import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.data.remote.dto.RecipeDetailDto
import com.quetzoft.recipes.data.remote.dto.RecipesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): RecipesResponseDto

    @GET("/recipes/{id}/information")
    suspend fun getRecipeDetail(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): RecipeDetailDto
}