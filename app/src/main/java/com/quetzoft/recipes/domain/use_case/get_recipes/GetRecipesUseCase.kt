package com.quetzoft.recipes.domain.use_case.get_recipes

import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.data.remote.dto.toRecipe
import com.quetzoft.recipes.data.remote.dto.toRecipeDetail
import com.quetzoft.recipes.domain.model.Recipe
import com.quetzoft.recipes.domain.model.RecipeDetail
import com.quetzoft.recipes.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetRecipesUseCase constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(recipeId: Int): Flow<Resource<List<Recipe>>> = flow {
        try {
            emit(Resource.Loading())
            val recipes = repository.getRecipes().map { it.toRecipe() }
            emit(Resource.Success(recipes))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}