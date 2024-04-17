package com.example.myrecipes.model.database.UserSavedRecipes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myrecipes.model.database.Recipes.Recipe
import com.example.myrecipes.model.database.User.User
import retrofit2.http.GET

@Dao
abstract interface UserSavedRecipesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: UserSavedRecipes)

    @Delete
    suspend fun delete(recipe: UserSavedRecipes)

    @Query("SELECT COUNT(*) FROM user_saved_recipes WHERE user_id = :userId AND recipe_id = :recipeId")
    fun getSavedRecipeCount(userId: Long, recipeId: String): Int

    @Query(" SELECT recipe_id FROM user_saved_recipes WHERE user_id = :userId")
    suspend fun getRecipesRelationByUser(userId: Long): List<String>?

}