package com.example.myrecipes.model.database.UserSavedRecipes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_saved_recipes", primaryKeys = ["user_id", "recipe_Id"])
data class UserSavedRecipes(
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "recipe_Id") val recipeId: String,
)