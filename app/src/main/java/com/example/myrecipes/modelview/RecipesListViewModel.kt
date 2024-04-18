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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

open class RecipesListViewModel(application: Application, private val workManager: WorkManager, private val repository: RecipesRepository) :
    AndroidViewModel(application) {
    private val recipeApi: RecipesApiRequest = RecipesApiRequest()
    private val logger = Logger.getLogger("MyLogger")

    private val _selectedCategories = MutableStateFlow<Set<String>>(setOf())

    data class UiState(
        val recipes: List<Recipe> = emptyList(),
        val viewableRecipes: List<Recipe> = emptyList(),
        val loading: Boolean = true,
        val error: Boolean = false,
        val page: Int? = null,
        val isFilterDialogOpen: Boolean = false,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

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
            ExistingPeriodicWorkPolicy.UPDATE,
            myWorkRequest as PeriodicWorkRequest
        )
        initalRecipesFetching()
    }

    fun toggleFilterDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                recipes = currentState.recipes,
                viewableRecipes = currentState.viewableRecipes,
                loading = currentState.loading,
                error = currentState.error,
                page = currentState.page,
                isFilterDialogOpen = !currentState.isFilterDialogOpen
            )
        }
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

        val updatedViewableRecipe = if (_selectedCategories.value.isEmpty()) {
            _uiState.value.recipes
        } else {
            _uiState.value.recipes.filter { recipe ->
                _selectedCategories.value.any { selectedCategory -> selectedCategory == recipe.strCategory }
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                recipes = currentState.recipes,
                viewableRecipes = updatedViewableRecipe,
                loading = currentState.loading,
                error = currentState.error,
                page = currentState.page,
                isFilterDialogOpen = currentState.isFilterDialogOpen
            )
        }
    }

    fun isCategorySelected(category: String): Boolean {
        return category in _selectedCategories.value
    }

    // Assuming you have a method to get all distinct categories from recipes
    fun getAllCategories(): List<String> {
        return _uiState.value.recipes.map { it.strCategory }.distinct()
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

                            _uiState.update { currentState ->
                                currentState.copy(
                                    recipes = recipes,
                                    viewableRecipes = recipes,
                                    loading = false,
                                    error = false,
                                    page = currentState.page,
                                    isFilterDialogOpen = currentState.isFilterDialogOpen
                                )
                            }
                        }
                    }

                    WorkInfo.State.FAILED -> {
//                        logger.warning("Work request failed")
                        _uiState.update { currentState ->
                            currentState.copy(
                                recipes = currentState.recipes,
                                viewableRecipes = currentState.viewableRecipes,
                                loading = currentState.loading,
                                error = true,
                                page = currentState.page,
                                isFilterDialogOpen = currentState.isFilterDialogOpen
                            )
                        }
                    }

                    WorkInfo.State.CANCELLED -> {
//                        logger.warning("Work request cancelled")
                    }

                    else -> {
                        // Work request is still running or enqueued
                    }
                }
            }
        }
    }

    fun getRecipeById(recipeId: String): Recipe? {
        return _uiState.value.recipes.firstOrNull { it.idMeal == recipeId}
    }

    private suspend fun fetchAdditionalRecipes(){
        for (i in 1..15) {
            val recipesRetrieved = recipeApi.fetchRecipes()
            recipesRetrieved.forEach { recipe ->
                logger.info("start fetching additional recipes")
                val updatedRecipes = uiState.value.recipes.toMutableList()
                updatedRecipes.add(recipe)

                _uiState.update { currentState ->
                    currentState.copy(
                        recipes = updatedRecipes,
                        viewableRecipes = currentState.viewableRecipes,
                        loading = currentState.loading,
                        error = currentState.error,
                        page = currentState.page,
                        isFilterDialogOpen = currentState.isFilterDialogOpen
                    )
                }
                repository.insertRecipes(recipe)
//                logger.info("finis fetching additional recipes")

            }
        }
    }

    internal fun setRecipes(recipes: List<Recipe>) {
        _uiState.update { currentState ->
            currentState.copy(
                recipes = recipes,
                viewableRecipes = currentState.viewableRecipes,
                loading = currentState.loading,
                error = currentState.error,
                page = currentState.page,
                isFilterDialogOpen = currentState.isFilterDialogOpen
            )
        }
    }

}
