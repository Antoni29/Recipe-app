package com.quetzoft.recipes.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.domain.model.Recipe
import com.quetzoft.recipes.domain.use_case.get_recipes.GetRecipesByCuisineUseCase
import com.quetzoft.recipes.domain.use_case.get_recipes.GetRecipesByQueryUseCase
import com.quetzoft.recipes.domain.use_case.get_recipes.GetRecipesOffsetUseCase
import com.quetzoft.recipes.domain.use_case.get_recipes.GetRecipesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @RelaxedMockK
    private lateinit var getRecipesUseCase: GetRecipesUseCase

    @RelaxedMockK
    private lateinit var getRecipesOffsetUseCase: GetRecipesOffsetUseCase

    @RelaxedMockK
    private lateinit var getRecipesByQueryUseCase: GetRecipesByQueryUseCase

    @RelaxedMockK
    private lateinit var getRecipesByCuisineUseCase: GetRecipesByCuisineUseCase

    private lateinit var viewModel: HomeViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(Dispatchers.Unconfined)

        viewModel = HomeViewModel(
            getRecipesUseCase,
            getRecipesOffsetUseCase,
            getRecipesByQueryUseCase,
            getRecipesByCuisineUseCase
        )
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when the ViewModel is created at the firs time, get all recipes and validate empty array response`() = runTest {
        //Given
        coEvery { getRecipesUseCase() } returns flowOf(Resource.Success(listOf()))

        //When
        viewModel.getRecipes()

        //Then
        assert(viewModel.recipes.value?.isEmpty() == true)
    }

    @Test
    fun `when the ViewModel is created at the firs time, get all recipes and set value to recipes variable`() = runTest {
        //Given
        val recipeList = listOf(
            Recipe(1, "Recipe 1", ""),
            Recipe(2, "Recipe 2", ""),
            Recipe(3, "Recipe 3", ""),
            Recipe(4, "Recipe 4", ""),
            Recipe(5, "Recipe 5", "")
        )
        coEvery { getRecipesUseCase() } returns flowOf(Resource.Success(recipeList))

        //When
        viewModel.getRecipes()

        //Then
        assert(viewModel.error.value?.isEmpty() == true)
        assert(viewModel.recipes.value?.isNotEmpty() == true)
        assert(viewModel.recipes.value?.first() == recipeList.first())
    }

    @Test
    fun `when the ViewModel is created at the firs time and gets error from GetRecipesUseCase`() = runTest {
        //Given
        coEvery { getRecipesUseCase() } returns flowOf(Resource.Error(Constants.UNEXPECTED_ERROR_MESSAGE))

        //When
        viewModel.getRecipes()

        //Then
        assert(viewModel.error.value?.isNotEmpty() == true)
        assert(viewModel.error.value == Constants.UNEXPECTED_ERROR_MESSAGE)
        assert(viewModel.recipes.value?.isEmpty() == true)
    }

    @Test
    fun `when the ViewModel execute the getRecipesOffset function with offset equals 5`() = runTest {
        //Given
        val offset = 5
        val recipeList = listOf(
            Recipe(1, "Recipe 1", ""),
            Recipe(2, "Recipe 2", ""),
            Recipe(3, "Recipe 3", ""),
            Recipe(4, "Recipe 4", ""),
            Recipe(5, "Recipe 5", "")
        )
        coEvery { getRecipesOffsetUseCase(offset) } returns flowOf(Resource.Success(recipeList))

        //When
        viewModel.getRecipesOffset(offset)

        //Then
        assert(viewModel.error.value?.isEmpty() == true)
        assert(viewModel.recipes.value?.size == offset)
        assert(viewModel.recipes.value?.first() == recipeList.first())
    }

    @Test
    fun `when the ViewModel execute the getRecipesOffset and gets error from GetRecipesOffsetUseCase`() = runTest {
        //Given
        val offset = 5
        coEvery { getRecipesOffsetUseCase(offset) } returns flowOf(Resource.Error(Constants.UNEXPECTED_ERROR_MESSAGE))

        //When
        viewModel.getRecipesOffset(offset)

        //Then
        assert(viewModel.error.value?.isNotEmpty() == true)
        assert(viewModel.error.value == Constants.UNEXPECTED_ERROR_MESSAGE)
        assert(viewModel.recipes.value?.isEmpty() == true)
    }

    @Test
    fun `when the ViewModel execute the getRecipesByQuery function with query equals pasta and offset equals 2`() = runTest {
        //Given
        val query = "pasta"
        val offset = 2
        val recipeList = listOf(
            Recipe(1, "Recipe 1 with pasta", ""),
            Recipe(2, "Recipe 2 with pasta", "")
        )
        coEvery { getRecipesByQueryUseCase(query, offset) } returns flowOf(Resource.Success(recipeList))

        //When
        viewModel.getRecipesByQuery(query, offset)

        //Then
        assert(viewModel.error.value?.isEmpty() == true)
        assert(viewModel.recipes.value?.size == offset)
        assert(viewModel.recipes.value?.first() == recipeList.first())
    }

    @Test
    fun `when the ViewModel execute the getRecipesByQuery and gets error from GetRecipesByQueryUseCase`() = runTest {
        //Given
        val query = "pasta"
        val offset = 2
        coEvery { getRecipesByQueryUseCase(query, offset) } returns flowOf(Resource.Error(Constants.UNEXPECTED_ERROR_MESSAGE))

        //When
        viewModel.getRecipesByQuery(query, offset)

        //Then
        assert(viewModel.error.value?.isNotEmpty() == true)
        assert(viewModel.error.value == Constants.UNEXPECTED_ERROR_MESSAGE)
        assert(viewModel.recipes.value?.isEmpty() == true)
    }

    @Test
    fun `when the ViewModel execute the getRecipesByCuisine function with query equals pasta and offset equals 3`() = runTest {
        //Given
        val cuisine = "mexican"
        val offset = 3
        val recipeList = listOf(
            Recipe(1, "Mexican recipe 1", ""),
            Recipe(2, "Mexican recipe 2", ""),
            Recipe(3, "Mexican recipe 3", "")
        )
        coEvery { getRecipesByCuisineUseCase(cuisine, offset) } returns flowOf(Resource.Success(recipeList))

        //When
        viewModel.getRecipesByCuisine(cuisine, offset)

        //Then
        assert(viewModel.error.value?.isEmpty() == true)
        assert(viewModel.recipes.value?.size == offset)
        assert(viewModel.recipes.value?.first() == recipeList.first())
        assert(viewModel.recipes.value?.first()?.title?.lowercase()!!.contains(cuisine))
    }

    @Test
    fun `when the ViewModel execute the getRecipesByCuisine and gets error from GetRecipesByCuisineUseCase`() = runTest {
        //Given
        val cuisine = "mexican"
        val offset = 3
        coEvery { getRecipesByCuisineUseCase(cuisine, offset) } returns flowOf(Resource.Error(Constants.UNEXPECTED_ERROR_MESSAGE))

        //When
        viewModel.getRecipesByCuisine(cuisine, offset)

        //Then
        assert(viewModel.error.value?.isNotEmpty() == true)
        assert(viewModel.error.value == Constants.UNEXPECTED_ERROR_MESSAGE)
        assert(viewModel.recipes.value?.isEmpty() == true)
    }
}