package com.quetzoft.recipes.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.domain.model.Recipe
import com.quetzoft.recipes.domain.use_case.get_recipes.GetRecipesByCuisineUseCase
import com.quetzoft.recipes.domain.use_case.get_recipes.GetRecipesByQueryUseCase
import com.quetzoft.recipes.domain.use_case.get_recipes.GetRecipesOffsetUseCase
import com.quetzoft.recipes.domain.use_case.get_recipes.GetRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getRecipesOffsetUseCase: GetRecipesOffsetUseCase,
    private val getRecipesByQueryUseCase: GetRecipesByQueryUseCase,
    private val getRecipesByCuisineUseCase: GetRecipesByCuisineUseCase
): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading = _isLoading

    private val _error = MutableLiveData<String>("")
    val error = _error

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes = _recipes

    init {
        getRecipes()
    }

    fun getRecipes(){
        getRecipesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _recipes.postValue(result.data ?: emptyList())
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

    fun getRecipesOffset(offset: Int){
        getRecipesOffsetUseCase(offset).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _recipes.postValue(result.data ?: emptyList())
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

    fun getRecipesByQuery(query: String, offset: Int){
        getRecipesByQueryUseCase(query, offset).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _recipes.postValue(result.data ?: emptyList())
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

    fun getRecipesByCuisine(cuisine: String, offset: Int){
        getRecipesByCuisineUseCase(cuisine, offset).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _recipes.postValue(result.data ?: emptyList())
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