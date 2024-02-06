package com.quetzoft.recipes.domain.use_case.get_recipes

import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.data.remote.dto.RecipeDto
import com.quetzoft.recipes.data.remote.dto.RecipesResponseDto
import com.quetzoft.recipes.data.remote.dto.toRecipe
import com.quetzoft.recipes.data.remote.dto.toRecipesResponse
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

class GetRecipesOffsetUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: RecipeRepository

    private lateinit var getRecipesOffsetUseCase: GetRecipesOffsetUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getRecipesOffsetUseCase = GetRecipesOffsetUseCase(repository)
    }

    @Test
    fun `when the api returns 5 items`() = runBlocking {
        //Given
        val offset = 5
        val recipeList = listOf(
            RecipeDto(1, "Recipe 1", ""),
            RecipeDto(2, "Recipe 2", ""),
            RecipeDto(3, "Recipe 3", ""),
            RecipeDto(4, "Recipe 4", ""),
            RecipeDto(5, "Recipe 5", "")
        )
        val recipesResponseDto = RecipesResponseDto(
            recipeList,
            offset,
            0,
            recipeList.size
        )
        coEvery { repository.getRecipes(offset) } returns recipesResponseDto

        //When
        val response = getRecipesOffsetUseCase(offset).toList()

        //Then
        val recipes = recipesResponseDto.toRecipesResponse().results
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Success)
        assert(response[1].data == recipes)
        assert(response[1].data?.size == offset)
    }

    @Test
    fun `when the api returns 10 items`() = runBlocking {
        //Given
        val offset = 10
        val recipeList = listOf(
            RecipeDto(1, "Recipe 1", ""),
            RecipeDto(2, "Recipe 2", ""),
            RecipeDto(3, "Recipe 3", ""),
            RecipeDto(4, "Recipe 4", ""),
            RecipeDto(5, "Recipe 5", ""),
            RecipeDto(6, "Recipe 6", ""),
            RecipeDto(7, "Recipe 7", ""),
            RecipeDto(8, "Recipe 8", ""),
            RecipeDto(9, "Recipe 9", ""),
            RecipeDto(10, "Recipe 10", "")
        )
        val recipesResponseDto = RecipesResponseDto(
            recipeList,
            offset,
            0,
            recipeList.size
        )
        coEvery { repository.getRecipes(offset) } returns recipesResponseDto

        //When
        val response = getRecipesOffsetUseCase(offset).toList()

        //Then
        val recipes = recipesResponseDto.toRecipesResponse().results
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Success)
        assert(response[1].data == recipes)
        assert(response[1].data?.size == offset)
    }

    @Test
    fun `when the api throws HttpException`() = runBlocking {
        //Given
        val httpStatusCode = 404
        val errorMessage = "HTTP 404 Response.error()"
        coEvery { repository.getRecipes(5) } throws HttpException(
            Response.error<String>(
                httpStatusCode,
                ResponseBody.create(null, errorMessage)
            )
        )

        //When
        val response = getRecipesOffsetUseCase(5).toList()

        //Then
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Error)
        assert(response[1].message == errorMessage)
    }

    @Test
    fun `when the api throws IOException`() = runBlocking {
        //Given
        coEvery { repository.getRecipes(5) } throws IOException()

        //When
        val response = getRecipesOffsetUseCase(5).toList()

        //Then
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Error)
        assert(response[1].message == Constants.IOEXCEPTION_MESSAGE)
    }
}