package com.example.myrecipes.modelview

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myrecipes.model.api.RecipesApiRequest
import com.example.myrecipes.model.database.Recipes.Recipe
import com.example.myrecipes.model.database.Recipes.RecipesRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.logging.Logger
import kotlin.math.log

class RefreshRecipesWorker (context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {
    private val recipeApi: RecipesApiRequest = RecipesApiRequest()
    private val logger = Logger.getLogger("MyLogger")
    private var repository: RecipesRepository

    init {
        repository = RecipesRepository(context)
    }

    override fun doWork(): Result {
        return try {
            var outputData: Data? = null // Declare outputData outside coroutine scope
            runBlocking { // Use runBlocking to block the current thread until coroutine completes
                CoroutineScope(Dispatchers.IO).launch {
                    logger.info("Fetching recipes in background...")
                    try {
                        val products = fetchRecipe()
                        logger.info("Recipes fetched")
                        logger.info("Start storing Recipes")
                        products.forEach { product ->
                            repository.insertRecipes(product)
                        }

                        logger.info("Finish storing Recipes")

                        // Create an output data object and put the products in it
                        outputData = Data.Builder().putString("Recipes", Gson().toJson(products)).build()
                    } catch (e: Exception) {
                        logger.warning(e.toString())
                    }
                }.join() // Wait for the coroutine to complete
            }

            // Return the result with outputData
            Result.success(outputData ?: Data.EMPTY)
        } catch (e: Exception) {
            logger.warning("Failed to fetch products in background: ${e.message}")
            Result.failure()
        }
    }
    private suspend fun fetchRecipe(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            var recipes: MutableList<Recipe> = mutableListOf()
            try {
                for (i in 1..5) {
                    val recipesRetrieved = recipeApi.fetchRecipes()
                    recipesRetrieved.forEach { recipe ->
                        recipes.add(recipe)
                    }
                }
                logger.info(recipes.toString())
                recipes
            } catch (e: Exception) {
                logger.warning("Failed to fetch recipes: ${e.message}")
                emptyList()
            }
        }
    }
}