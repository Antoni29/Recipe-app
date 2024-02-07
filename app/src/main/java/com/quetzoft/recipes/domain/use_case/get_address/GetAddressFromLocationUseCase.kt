package com.quetzoft.recipes.domain.use_case.get_address

import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.common.Resource
import com.quetzoft.recipes.domain.repository.RecipeMapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetAddressFromLocationUseCase@Inject constructor(
    private val repository: RecipeMapRepository
) {
    operator fun invoke(lat: Double, lng: Double): Flow<Resource<String>> = flow {
        try {
            val address = repository.getAddressFromLocation(lat, lng)
            emit(Resource.Success(address))
        } catch (e: IOException) {
            emit(Resource.Error(Constants.IOEXCEPTION_MESSAGE))
        }
    }
}