package com.quetzoft.recipes.presentation.recipe_map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.domain.use_case.get_address.GetAddressFromLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeMapViewModel @Inject constructor(
    private val getAddressFromLocationUseCase: GetAddressFromLocationUseCase
): ViewModel() {

    private val _address = MutableLiveData<String>("")
    val address = _address

    fun getAddressFromLocation(lat: Double, lng: Double) {
        getAddressFromLocationUseCase(lat, lng).onEach { result ->
            if(result is Resource.Success) {
                _address.postValue(result.data ?: "")
            }
        }.launchIn(viewModelScope)
    }
}