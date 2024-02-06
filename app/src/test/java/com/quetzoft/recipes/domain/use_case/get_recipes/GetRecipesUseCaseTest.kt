package com.quetzoft.recipes.domain.use_case.get_recipes

import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.data.remote.dto.RecipeDto
import com.quetzoft.recipes.data.remote.dto.RecipesResponseDto
import com.quetzoft.recipes.data.remote.dto.toRecipe
import com.quetzoft.recipes.domain.model.Recipe
import com.quetzoft.recipes.domain.repository.RecipeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class GetRecipesUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: RecipeRepository

    private lateinit var getRecipesUseCase: GetRecipesUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getRecipesUseCase = GetRecipesUseCase(repository)
    }

    @Test
    fun `when the api returns empty`() = runBlocking {
        //Given
        val recipeList = emptyList<RecipeDto>()
        coEvery { repository.getRecipes() } returns RecipesResponseDto(recipeList, 10, 0, 0)

        //When
        val response = getRecipesUseCase().toList()

        //Then
        assert(response[1].data == emptyList<Recipe>())
    }

    @Test
    fun `when the api returns something`() = runBlocking {
        //Given
        val recipeList = listOf(RecipeDto(1, "Recipe 1", ""))
        coEvery { repository.getRecipes() } returns RecipesResponseDto(recipeList, 10, 0, 1)

        //When
        val response = getRecipesUseCase().toList()

        //Then
        val recipes = recipeList.map { it.toRecipe() }
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Success)
        assert(response[1].data == recipes)
    }

    @Test
    fun `when the api throws HttpException`() = runBlocking {
        //Given
        val httpStatusCode = 404
        val errorMessage = "HTTP 404 Response.error()"
        coEvery { repository.getRecipes() } throws HttpException(
            Response.error<String>(
                httpStatusCode,
                ResponseBody.create(null, errorMessage)
            )
        )

        //When
        val response = getRecipesUseCase().toList()

        //Then
        assert(response[1] is Resource.Error)
        assert(response[1].message == errorMessage)
    }

    @Test
    fun `when the api throws IOException`() = runBlocking {
        //Given
        coEvery { repository.getRecipes() } throws IOException()

        //When
        val response = getRecipesUseCase().toList()

        //Then
        assert(response[1] is Resource.Error)
        assert(response[1].message == Constants.IOEXCEPTION_MESSAGE)
    }
}