package com.quetzoft.recipes.domain.use_case.get_address

import com.quetzoft.recipes.domain.repository.RecipeMapRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAddressFromLocationUseCaseTest {

    @RelaxedMockK
    private lateinit var recipeMapRepository: RecipeMapRepository

    private lateinit var getAddressFromLocationUseCase: GetAddressFromLocationUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getAddressFromLocationUseCase = GetAddressFromLocationUseCase(recipeMapRepository)
    }

    @Test
    fun `when RecipeMapRepository returns empty address`() = runBlocking {
        //Given
        val lat = 0.0
        val lng = 0.0
        val address = ""
        coEvery { recipeMapRepository.getAddressFromLocation(lat, lng) } returns address

        //When
        val response = getAddressFromLocationUseCase(lat, lng).toList()

        //Then
        assert(response[0].data?.isEmpty() == true)
    }

    @Test
    fun `when RecipeMapRepository returns an address`() = runBlocking {
        //Given
        val lat = 20.6395
        val lng = -100.4008
        val address = "Street 23, My Local Town"
        coEvery { recipeMapRepository.getAddressFromLocation(lat, lng) } returns address

        //When
        val response = getAddressFromLocationUseCase(lat, lng).toList()

        //Then
        assert(response[0].data?.isNotEmpty() == true)
        assert(response[0].data == address)
    }
}