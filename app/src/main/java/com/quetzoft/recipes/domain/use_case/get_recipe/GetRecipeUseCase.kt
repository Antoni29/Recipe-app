package com.quetzoft.recipes.domain.use_case.get_recipe

import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.data.remote.dto.toRecipeDetail
import com.quetzoft.recipes.domain.model.RecipeDetail
import com.quetzoft.recipes.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(recipeId: Int): Flow<Resource<RecipeDetail>> = flow {
        try {
            emit(Resource.Loading())
            val recipe = repository.getRecipeDetail(recipeId).toRecipeDetail()
            emit(Resource.Success(recipe))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}