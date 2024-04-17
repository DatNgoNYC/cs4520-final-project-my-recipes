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
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
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

    fun calculateObjectSize(obj: Any): Int {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        objectOutputStream.flush()
        objectOutputStream.close()
        return byteArrayOutputStream.toByteArray().size
    }

    override fun doWork(): Result {
        return try {
            var outputData: Data? = null // Declare outputData outside coroutine scope
            runBlocking { // Use runBlocking to block the current thread until coroutine completes
                CoroutineScope(Dispatchers.IO).launch {
                    logger.info("Fetching recipes in background...")
                    try {
                        val recipes = fetchRecipe()
                        logger.info("Recipes fetched")
                        logger.info("Start storing Recipes")
                        var byteCount = 0
                        recipes.forEach { recipe ->
                            val serializedSize = calculateObjectSize(recipe)
                            byteCount += serializedSize
                            logger.info(byteCount.toString())
                            if (byteCount > 10240) {
                                // Skip inserting this recipe
                            } else {
                                repository.insertRecipes(recipe)
                            }
                        }
                        logger.info("Finish storing Recipes")

                        try {
                            // Create an output data object and put the products in it
                            outputData =
                                Data.Builder().putString("Recipes", Gson().toJson(recipes)).build()
                        }catch (e: Exception){
                            logger.warning(e.toString())
                        }
                    } catch (e: IllegalStateException) {
                        logger.info("too big")
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
                for (i in 1..4) {
                    val recipesRetrieved = recipeApi.fetchRecipes()
                    recipesRetrieved.forEach { recipe ->
                        recipes.add(recipe)
                    }
                }
                recipes
            } catch (e: Exception) {
                logger.warning("Failed to fetch recipes: ${e.message}")
                emptyList()
            }
        }
    }
}