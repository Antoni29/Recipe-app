package com.quetzoft.recipes.presentation.recipe_detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.domain.model.RecipeDetail
import com.quetzoft.recipes.domain.use_case.get_recipe.GetRecipeUseCase
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
class RecipeDetailViewModelTest {

    @RelaxedMockK
    private lateinit var getRecipeUseCase: GetRecipeUseCase

    private lateinit var viewModel: RecipeDetailViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(Dispatchers.Unconfined)

        viewModel = RecipeDetailViewModel(getRecipeUseCase)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when the ViewModel execute the getRecipeDetail function and set value to recipe variable`() = runTest {
        //Given
        val recipeId = 1
        val recipeDetail = RecipeDetail(
            recipeId,
            "Recipe 1",
            "",
            "jpeg",
            80,
            30,
            "Summary Recipe 1",
            "Instructions",
            arrayListOf()
        )
        coEvery { getRecipeUseCase(recipeId) } returns flowOf(Resource.Success(recipeDetail))

        //When
        viewModel.getRecipeDetail(recipeId)

        //Then
        assert(viewModel.error.value?.isEmpty() == true)
        assert(viewModel.recipe.value == recipeDetail)
    }



    @Test
    fun `when the ViewModel execute the getRecipeDetail function and gets error from GetRecipeUseCase`() = runTest {
        //Given
        val recipeId = 1
        coEvery { getRecipeUseCase(recipeId) } returns flowOf(Resource.Error(Constants.UNEXPECTED_ERROR_MESSAGE))

        //When
        viewModel.getRecipeDetail(recipeId)

        //Then
        assert(viewModel.error.value?.isNotEmpty() == true)
        assert(viewModel.error.value == Constants.UNEXPECTED_ERROR_MESSAGE)
    }
}