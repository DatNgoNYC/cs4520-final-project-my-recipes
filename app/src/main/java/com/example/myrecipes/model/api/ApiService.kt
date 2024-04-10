package com.example.myrecipes.model.api

import com.example.myrecipes.model.database.Recipes.Recipe
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("random.php")
    suspend fun getRandomRecipe(): Response<List<Recipe>>

    @GET("random.php")
    suspend fun getRecipeByName(name: String): Response<List<Recipe>>
}