package com.example.myrecipes.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.myrecipes.model.api.RecipesApiRequest
import com.example.myrecipes.model.database.Recipes.Recipe
import com.example.myrecipes.model.database.Recipes.RecipesRepository
import com.example.myrecipes.model.database.UserSavedRecipes.UserSavedRecipesRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class RecipesListViewModel(application: Application, private val workManager: WorkManager, val repository: RecipesRepository) :
    AndroidViewModel(application) {
    private val recipeApi: RecipesApiRequest = RecipesApiRequest()
    private val logger = Logger.getLogger("MyLogger")

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _viewableRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val viewableRecipes: StateFlow<List<Recipe>> = _viewableRecipes

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error

    private val _page = MutableStateFlow<Int?>(null)
    val page: StateFlow<Int?> = _page

    private val _selectedCategories = MutableStateFlow<Set<String>>(setOf())
    private val _isFilterDialogOpen = MutableStateFlow(false)
    val isFilterDialogOpen: StateFlow<Boolean> = _isFilterDialogOpen

    private val constraints = Constraints.Builder()
        .setRequiresCharging(false)
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    init {
        viewModelScope.launch {
            fetchAdditionalRecipes()
        }

        val myWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<RefreshRecipesWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        workManager.enqueueUniquePeriodicWork(
            "product_refresh_work",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest as PeriodicWorkRequest
        )
        initalRecipesFetching()
    }

    fun toggleFilterDialog() {
        _isFilterDialogOpen.value = !_isFilterDialogOpen.value
    }

    fun updateCategorySelection(category: String, isSelected: Boolean) {
        val currentSelections = _selectedCategories.value.toMutableSet()
        if (isSelected) {
            currentSelections.add(category)
        } else {
            currentSelections.remove(category)
        }
        _selectedCategories.value = currentSelections
        applyFilters()
    }

    private fun applyFilters() {
        _viewableRecipes.value = if (_selectedCategories.value.isEmpty()) {
            _recipes.value
        } else {
            _recipes.value.filter { recipe ->
                _selectedCategories.value.any { selectedCategory -> selectedCategory == recipe.strCategory }
            }
        }
    }

    fun isCategorySelected(category: String): Boolean {
        return category in _selectedCategories.value
    }

    // Assuming you have a method to get all distinct categories from recipes
    fun getAllCategories(): List<String> {
        return _recipes.value.map { it.strCategory }.distinct()
    }

    private fun convertJsonToProducts(json: String?): List<Recipe> {
        val listType = object : TypeToken<List<Recipe>>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun initalRecipesFetching() {
        val refreshWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<RefreshRecipesWorker>()
                .setConstraints(constraints)
                .build()

        workManager.enqueue(refreshWorkRequest)

        workManager.getWorkInfoByIdLiveData(refreshWorkRequest.id).observeForever { workInfo ->
            if (workInfo != null) {
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        logger.info("Work request succeeded")
                        val outputData = workInfo.outputData
                        val productsJson = outputData.getString("Recipes")

                        if (productsJson != null) {
                            val recipes: List<Recipe> = convertJsonToProducts(productsJson)
                            _loading.value = false
                            _recipes.value = recipes
                            _viewableRecipes.value = recipes
                            _error.value = false
                        }else{
                            fetchProductsFromDatabase()
                            _loading.value = false
                            _error.value = false
                        }
                    }

                    WorkInfo.State.FAILED -> {
                        logger.info("failed")
                        logger.warning("Work request failed")
                        fetchProductsFromDatabase()
                        if (this._recipes.value.size != 0) {
                            _error.value = false
                            _loading.value = false
                        }
                     }

                    WorkInfo.State.CANCELLED -> {
                        logger.warning("Work request cancelled")
                    }

                    else -> {
                        logger.info("else")
                        fetchProductsFromDatabase()
                        _loading.value = false
                        _error.value = false
                    }
                }
            }
        }
    }

    fun getRecipeById(recipeId: String): Recipe? {
        return recipes.value.firstOrNull { it.idMeal == recipeId}
    }

    private suspend fun fetchAdditionalRecipes(){
        for (i in 1..15) {
            val recipesRetrieved = recipeApi.fetchRecipes()
            recipesRetrieved.forEach { recipe ->
                logger.info("start fetching additional recipes")
                val updatedRecipes = _recipes.value.toMutableList()
                updatedRecipes.add(recipe)
                _recipes.value = updatedRecipes
                repository.insertRecipes(recipe)
                logger.info("finis fetching additional recipes")

            }
        }
    }

    internal fun setRecipes(recipes: List<Recipe>) {
        _recipes.value = recipes
    }

    // in case of there is no internet retrieve product from the databse
    private fun fetchProductsFromDatabase() {
        val currentPage = _page.value ?: 1

        viewModelScope.launch {
            try {
                logger.info("start talking to the database")
                val databaseProducts = repository.getRecipes(currentPage)
                logger.info("Database fetch size: ${databaseProducts.size}")
                _viewableRecipes.value = databaseProducts
                logger.info(_viewableRecipes.value.toString())
                logger.info("Successfully fetched products from database")
            } catch (e: Exception) {
                logger.warning("Failed to fetch products from database: ${e.message}")
            }
        }
    }
}
