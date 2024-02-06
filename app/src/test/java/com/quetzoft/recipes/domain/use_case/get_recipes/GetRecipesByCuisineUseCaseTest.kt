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


class GetRecipesByCuisineUseCaseTest {
    @RelaxedMockK
    private lateinit var repository: RecipeRepository

    private lateinit var getRecipesByCuisineUseCase: GetRecipesByCuisineUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getRecipesByCuisineUseCase = GetRecipesByCuisineUseCase(repository)
    }

    @Test
    fun `when the api returns 5 items of Mexican food`() = runBlocking {
        //Given
        val cuisine = "mexican"
        val offset = 5
        val recipeList = listOf(
            RecipeDto(1, "Mexican recipe 1", ""),
            RecipeDto(2, "Mexican recipe 2", ""),
            RecipeDto(3, "Mexican recipe 3", ""),
            RecipeDto(4, "Mexican recipe 4", ""),
            RecipeDto(5, "Mexican recipe 5", "")
        )
        val recipesResponseDto = RecipesResponseDto(
            recipeList,
            offset,
            0,
            recipeList.size
        )
        coEvery { repository.getRecipesByCuisine(cuisine, offset) } returns recipesResponseDto

        //When
        val response = getRecipesByCuisineUseCase(cuisine, offset).toList()

        //Then
        val recipes = recipesResponseDto.toRecipesResponse().results
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Success)
        assert(response[1].data == recipes)
        assert(response[1].data?.size == offset)
        assert(response[1].data?.get(0)?.title?.lowercase()!!.contains(cuisine))
    }

    @Test
    fun `when the api returns 10 items of Japanise food`() = runBlocking {
        //Given
        val cuisine = "japanese"
        val offset = 10
        val recipeList = listOf(
            RecipeDto(1, "Japanese recipe 1", ""),
            RecipeDto(2, "Japanese recipe 2", ""),
            RecipeDto(3, "Japanese recipe 3", ""),
            RecipeDto(4, "Japanese recipe 4", ""),
            RecipeDto(5, "Japanese recipe 5", ""),
            RecipeDto(6, "Japanese recipe 6", ""),
            RecipeDto(7, "Japanese recipe 7", ""),
            RecipeDto(8, "Japanese recipe 8", ""),
            RecipeDto(9, "Japanese recipe 9", ""),
            RecipeDto(10, "Japanese recipe 10", "")
        )
        val recipesResponseDto = RecipesResponseDto(
            recipeList,
            offset,
            0,
            recipeList.size
        )
        coEvery { repository.getRecipesByCuisine(cuisine, offset) } returns recipesResponseDto

        //When
        val response = getRecipesByCuisineUseCase(cuisine, offset).toList()

        //Then
        val recipes = recipesResponseDto.toRecipesResponse().results
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Success)
        assert(response[1].data == recipes)
        assert(response[1].data?.size == offset)
        assert(response[1].data?.get(0)?.title?.lowercase()!!.contains(cuisine))
    }

    @Test
    fun `when the api not found items by cuisine`() = runBlocking {
        //Given
        val cuisine = "mediterranean"
        val offset = 0
        val recipeList = listOf<RecipeDto>()
        val recipesResponseDto = RecipesResponseDto(
            recipeList,
            offset,
            0,
            0
        )
        coEvery { repository.getRecipesByCuisine(cuisine, offset) } returns recipesResponseDto

        //When
        val response = getRecipesByCuisineUseCase(cuisine, offset).toList()

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
        val cuisine = "mexican"
        val offset = 2
        val httpStatusCode = 404
        val errorMessage = "HTTP 404 Response.error()"
        coEvery { repository.getRecipesByCuisine(cuisine, offset) } throws HttpException(
            Response.error<String>(
                httpStatusCode,
                ResponseBody.create(null, errorMessage)
            )
        )

        //When
        val response = getRecipesByCuisineUseCase(cuisine, offset).toList()

        //Then
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Error)
        assert(response[1].message == errorMessage)
    }

    @Test
    fun `when the api throws IOException`() = runBlocking {
        //Given
        val cuisine = "mexican"
        val offset = 2
        coEvery { repository.getRecipesByCuisine(cuisine, offset) } throws IOException()

        //When
        val response = getRecipesByCuisineUseCase(cuisine, offset).toList()

        //Then
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Error)
        assert(response[1].message == Constants.IOEXCEPTION_MESSAGE)
    }
}