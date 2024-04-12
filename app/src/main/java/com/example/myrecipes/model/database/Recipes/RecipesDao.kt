package com.example.myrecipes.model.database.Recipes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe)

    @Insert
    suspend fun insertAll(products: List<Recipe>)

    @Query("SELECT * FROM recipes_table LIMIT :pageSize OFFSET :offset")
    suspend fun getRecipesByPage(pageSize: Int, offset: Int): List<Recipe>

    @Query("SELECT * FROM recipes_table WHERE strMeal= :recipeName")
    suspend fun getRecipeByName(recipeName: String): Recipe?
}