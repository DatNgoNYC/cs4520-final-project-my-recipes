package com.example.myrecipes.model.database.UserSavedRecipes

import android.content.Context
import com.example.myrecipes.model.database.AppDatabase
import com.example.myrecipes.model.database.Recipes.Recipe
import com.example.myrecipes.model.database.User.User
import com.example.myrecipes.model.database.User.UserDao
import java.util.logging.Logger

class UserSavedRecipesRepository(context: Context) {
    private val logger = Logger.getLogger("MyLogger")

    private var userSavedRecipesDao: UserSavedRecipesDao

    private val database = AppDatabase.getInstance(context)

    init {
        userSavedRecipesDao = database.UserSavedRecipesDao()
    }

    suspend fun addSavedRecipes(savedRecipes : UserSavedRecipes){
        logger.info("adding saved recipes")
        userSavedRecipesDao.insert(savedRecipes)
        logger.info("finish saving recipes")

    }

    suspend fun removeSavedRecipes(savedRecipes : UserSavedRecipes){
        logger.info("removing saved recipes")
        userSavedRecipesDao.delete(savedRecipes)
        logger.info("finish removing recipes")

    }

    fun getSavedRecipe(savedRecipes : UserSavedRecipes): Int{
        return userSavedRecipesDao.getSavedRecipeCount(savedRecipes.userId, savedRecipes.recipeId)
    }

}