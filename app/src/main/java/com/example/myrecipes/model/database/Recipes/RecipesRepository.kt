package com.example.myrecipes.model.database.Recipes

import android.content.Context
import com.example.myrecipes.model.database.AppDatabase

class RecipesRepository(context: Context) {

    private var recipesDao: RecipesDao
    private val database = AppDatabase.getInstance(context)

    init {
        recipesDao = database.RecipeDao()
    }

    // retrieve the product base on the page number
    suspend fun getRecipes(page: Int ): List<Recipe> {
        val pageSize = 30
        val offset = (page - 1) * pageSize
        return recipesDao.getRecipesByPage(pageSize, offset)
    }

    suspend fun insertProduct(recipe: Recipe) {
        recipesDao.insert(recipe)
    }
    suspend fun getRecipeByName(recipeName: String) {
        recipesDao.getRecipeByName(recipeName)
    }

}