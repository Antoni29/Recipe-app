package com.quetzoft.recipes.domain.use_case.get_recipes

import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.data.remote.dto.toRecipe
import com.quetzoft.recipes.data.remote.dto.toRecipeDetail
import com.quetzoft.recipes.data.remote.dto.toRecipesResponse
import com.quetzoft.recipes.domain.model.Recipe
import com.quetzoft.recipes.domain.model.RecipeDetail
import com.quetzoft.recipes.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<Resource<List<Recipe>>> = flow {
        try {
            emit(Resource.Loading())
            val recipes = repository.getRecipes().toRecipesResponse().results
            emit(Resource.Success(recipes))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR_MESSAGE))
        } catch(e: IOException) {
            emit(Resource.Error(Constants.IOEXCEPTION_MESSAGE))
        }
    }
}