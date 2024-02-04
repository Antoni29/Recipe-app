package com.quetzoft.recipes.domain.use_case.get_recipes

import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.data.remote.dto.toRecipesResponse
import com.quetzoft.recipes.domain.model.Recipe
import com.quetzoft.recipes.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRecipesOffsetUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(offset: Int): Flow<Resource<List<Recipe>>> = flow {
        try {
            emit(Resource.Loading())
            val recipes = repository.getRecipes(offset = offset).toRecipesResponse().results
            emit(Resource.Success(recipes))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}