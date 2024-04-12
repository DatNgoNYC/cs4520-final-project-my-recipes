package com.example.myrecipes.model.api

import com.example.myrecipes.model.database.Recipes.Recipe
import com.google.gson.annotations.SerializedName

data class MealResponse(
    @SerializedName("meals")
    val meals: List<Recipe>
)