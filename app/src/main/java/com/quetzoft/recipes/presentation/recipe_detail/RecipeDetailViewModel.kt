package com.quetzoft.recipes.presentation.recipe_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.domain.model.RecipeDetail
import com.quetzoft.recipes.domain.use_case.get_recipe.GetRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase
): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading = _isLoading

    private val _error = MutableLiveData<String>("")
    val error = _error

    private val _recipe = MutableLiveData<RecipeDetail?>()
    val recipe: LiveData<RecipeDetail?> = _recipe

    fun getRecipeDetail(recipeId: Int) {
        getRecipeUseCase(recipeId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _recipe.postValue(result.data)
                }
                is Resource.Error -> {
                    _isLoading.postValue(false)
                    _error.postValue(result.message ?: "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _isLoading.postValue(true)
                }
            }
        }.launchIn(viewModelScope)
    }
}