package com.example.myrecipes.model.api

import com.example.myrecipes.model.database.Recipes.Recipe
import java.util.logging.Logger

class RecipesApiRequest {
    private val apiService = ApiClient.ApiClient.apiService
    private val logger = Logger.getLogger("MyLogger")

    // fetch the product from the api
    suspend fun fetchProducts(): List<Recipe> {
        try {
            logger.info("Start fetching products")
            val response = apiService.getRandomRecipe()
            if (response.isSuccessful) {
                val recipeList = response.body()
                if (recipeList != null) {
                    logger.info("Products fetched successfully")
                    return recipeList.distinctBy { it.idMeal }
                } else {
                    logger.warning("Response body is null")
                }
            } else {
                logger.warning("Failed to fetch products: ${response.code()}")
            }
        } catch (e: Exception) {
            logger.severe("Failed to fetch products: ${e.message}")
        }

        return emptyList()
    }

    suspend fun fetchProductsByName(name: String): List<Recipe> {
        try {
            logger.info("Start fetching products")
            val response = apiService.getRecipeByName(name)
            if (response.isSuccessful) {
                val recipeList = response.body()
                if (recipeList != null) {
                    logger.info("Products fetched successfully")
                    return recipeList.distinctBy { it.idMeal }
                } else {
                    logger.warning("Response body is null")
                }
            } else {
                logger.warning("Failed to fetch products: ${response.code()}")
            }
        } catch (e: Exception) {
            logger.severe("Failed to fetch products: ${e.message}")
        }

        return emptyList()
    }
}