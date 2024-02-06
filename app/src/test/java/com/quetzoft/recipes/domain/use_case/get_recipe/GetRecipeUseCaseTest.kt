package com.quetzoft.recipes.domain.use_case.get_recipe

import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.data.remote.dto.RecipeDetailDto
import com.quetzoft.recipes.data.remote.dto.toRecipeDetail
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

class GetRecipeUseCaseTest {
    @RelaxedMockK
    private lateinit var repository: RecipeRepository

    private lateinit var getRecipeUseCase: GetRecipeUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getRecipeUseCase = GetRecipeUseCase(repository)
    }

    @Test
    fun `when the api returns recipe detail`() = runBlocking {
        //Given
        val recipeDetailDto = RecipeDetailDto(
            1,
            "Recipe 1",
            "",
            "jpeg",
            80,
            30,
            "Summary Recipe 1",
            "Instructions",
            arrayListOf()
        )
        coEvery { repository.getRecipeDetail(1) } returns recipeDetailDto

        //When
        val response = getRecipeUseCase(1).toList()

        //Then
        val recipeDetail = recipeDetailDto.toRecipeDetail()
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Success)
        assert(response[1].data == recipeDetail)
        assert(response[1].data?.title == recipeDetail.title)
    }

    @Test
    fun `when the api throws HttpException`() = runBlocking {
        //Given
        val httpStatusCode = 404
        val errorMessage = "HTTP 404 Response.error()"
        coEvery { repository.getRecipeDetail(1) } throws HttpException(
            Response.error<String>(
                httpStatusCode,
                ResponseBody.create(null, errorMessage)
            )
        )

        //When
        val response = getRecipeUseCase(1).toList()

        //Then
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Error)
        assert(response[1].message == errorMessage)
    }

    @Test
    fun `when the api throws IOException`() = runBlocking {
        //Given
        coEvery { repository.getRecipeDetail(1) } throws IOException()

        //When
        val response = getRecipeUseCase(1).toList()

        //Then
        assert(response[0] is Resource.Loading)
        assert(response[1] is Resource.Error)
        assert(response[1].message == Constants.IOEXCEPTION_MESSAGE)
    }
}