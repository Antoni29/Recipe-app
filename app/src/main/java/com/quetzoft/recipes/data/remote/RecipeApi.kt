package com.quetzoft.recipes.data.remote

import com.quetzoft.recipes.data.remote.dto.RecipeDetailDto
import com.quetzoft.recipes.data.remote.dto.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(): List<RecipeDto>

    @GET("/recipes/{id}/information")
    suspend fun getRecipeDetail(@Path("id") recipeId: Int): RecipeDetailDto
}