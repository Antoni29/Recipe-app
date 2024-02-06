package com.quetzoft.recipes.domain.use_case.get_recipes

import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.data.remote.dto.RecipeDto
import com.quetzoft.recipes.data.remote.dto.RecipesResponseDto
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

class GetRecipesByQueryUseCaseTest {
    @RelaxedMockK
    private lateinit var repository: RecipeRepository

    private lateinit var getRecipesByQueryUseCase: GetRecipesByQueryUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getRecipesByQueryUseCase = GetRecipesByQueryUseCase(repository)
    }

    @Test
    fun `when the api returns 5 items of Pasta`() = runBlocking {
        //Given
        val query = "pasta"
        val offset = 5
        val recipeList = listOf(
            RecipeDto(1, "Recipe 1 with pasta", ""),
            RecipeDto(2, "Recipe 2 with pasta", ""),
            RecipeDto(3, "Recipe 3 with pasta", ""),
            RecipeDto(4, "Recipe 4 with pasta", ""),
            RecipeDto(5, "Recipe 5 with pasta", "")
        )
        val recipesResponseDto = RecipesResponseDto(
            recipeList,
            offset,
            0,
            recipeList.size
        )
        coEvery { repository.getRecipesByQuery(query, offset) } returns recipesResponseDto

        //When
        val response = getRecipesByQueryUseCase(query, offset).toList()

        //Then
        val recipes = recipesResponseDto.toRecipesResponse().results
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Success)
        assert(response[1].data == recipes)
        assert(response[1].data?.size == offset)
        assert(response[1].data?.get(0)?.title?.lowercase()!!.contains(query))
    }

    @Test
    fun `when the api not found items by query`() = runBlocking {
        //Given
        val query = "cake"
        val offset = 0
        val recipeList = listOf<RecipeDto>()
        val recipesResponseDto = RecipesResponseDto(
            recipeList,
            offset,
            0,
            0
        )
        coEvery { repository.getRecipesByQuery(query, offset) } returns recipesResponseDto

        //When
        val response = getRecipesByQueryUseCase(query, offset).toList()

        //Then
        val recipes = recipesResponseDto.toRecipesResponse().results
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Success)
        assert(response[1].data == recipes)
        assert(response[1].data?.isEmpty() == true)
    }

    @Test
    fun `when the api throws HttpException`() = runBlocking {
        //Given
        val query = "pasta"
        val offset = 2
        val httpStatusCode = 404
        val errorMessage = "HTTP 404 Response.error()"
        coEvery { repository.getRecipesByQuery(query, offset) } throws HttpException(
            Response.error<String>(
                httpStatusCode,
                ResponseBody.create(null, errorMessage)
            )
        )

        //When
        val response = getRecipesByQueryUseCase(query, offset).toList()

        //Then
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Error)
        assert(response[1].message == errorMessage)
    }

    @Test
    fun `when the api throws IOException`() = runBlocking {
        //Given
        val query = "pasta"
        val offset = 2
        coEvery { repository.getRecipesByQuery(query, offset) } throws IOException()

        //When
        val response = getRecipesByQueryUseCase(query, offset).toList()

        //Then
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Error)
        assert(response[1].message == Constants.IOEXCEPTION_MESSAGE)
    }
}