package com.example.myrecipes.model.api

import com.example.myrecipes.model.database.Recipes.Recipe
import java.util.logging.Logger

class RecipesApiRequest {
    private val apiService = ApiClient.apiService
    private val logger = Logger.getLogger("MyLogger")

    // fetch the recieps from the api
    suspend fun fetchRecipes(): List<Recipe> {
        try {
            logger.info("Start fetching recipes")
            val response = apiService.getRandomRecipe()
            if (response.isSuccessful) {
                val mealResponse = response.body()
                if (mealResponse != null) {
                    logger.info("Recipes fetched successfully")
                    return mealResponse.meals
                } else {
                    logger.warning("Meal response body is null")
                }
            } else {
                logger.warning("Failed to fetch recipes: ${response.code()}")
            }
        } catch (e: Exception) {
            logger.severe("Failed to fetch recipes: ${e.message}")
        }

        return emptyList()
    }

    suspend fun fetchProductsByName(name: String): List<Recipe> {
        try {
            logger.info("Start fetching recipes by name")
            val response = apiService.getRecipeByName(name)
            if (response.isSuccessful) {
                val mealResponse = response.body()
                if (mealResponse != null) {
                    logger.info("Recipes fetched successfully")
                    return mealResponse.meals
                } else {
                    logger.warning("Meal response body is null")
                }
            } else {
                logger.warning("Failed to fetch recipes: ${response.code()}")
            }
        } catch (e: Exception) {
            logger.severe("Failed to fetch recipes: ${e.message}")
        }

        return emptyList()
    }
}