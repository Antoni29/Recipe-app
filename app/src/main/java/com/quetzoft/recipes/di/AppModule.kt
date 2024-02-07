package com.quetzoft.recipes.di

import android.content.Context
import android.location.Geocoder
import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.data.remote.RecipeApi
import com.quetzoft.recipes.data.repository.RecipeMapRepositoryImpl
import com.quetzoft.recipes.data.repository.RecipeRepositoryImpl
import com.quetzoft.recipes.domain.repository.RecipeMapRepository
import com.quetzoft.recipes.domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRecipeApi(): RecipeApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(api: RecipeApi): RecipeRepository {
        return RecipeRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGeocoder(@ApplicationContext context: Context): Geocoder {
        return Geocoder(context, Locale.getDefault())
    }

    @Provides
    @Singleton
    fun provideRecipeMapRepository(geocoder: Geocoder): RecipeMapRepository {
        return RecipeMapRepositoryImpl(geocoder)
    }
}