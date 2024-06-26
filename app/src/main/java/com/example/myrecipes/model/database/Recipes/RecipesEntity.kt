package com.example.myrecipes.model.database.Recipes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Recipes_table")
data class Recipe (
    @PrimaryKey val idMeal: String,
    @ColumnInfo(name = "strMeal") val strMeal: String,
    @ColumnInfo(name = "strDrinkAlternate") val strDrinkAlternate: String?,
    @ColumnInfo(name = "strCategory") val strCategory: String,
    @ColumnInfo(name = "strArea") val strArea: String,
    @ColumnInfo(name = "strInstructions") val strInstructions: String,
    @ColumnInfo(name = "strMealThumb") val strMealThumb: String,
    @ColumnInfo(name = "strTags") val strTags: String,
    @ColumnInfo(name = "strYoutube") val strYoutube: String,
    @ColumnInfo(name = "strIngredient1") val strIngredient1: String,
    @ColumnInfo(name = "strIngredient2") val strIngredient2: String,
    @ColumnInfo(name = "strIngredient3") val strIngredient3: String,
    @ColumnInfo(name = "strIngredient4") val strIngredient4: String,
    @ColumnInfo(name = "strIngredient5") val strIngredient5: String,
    @ColumnInfo(name = "strIngredient6") val strIngredient6: String,
    @ColumnInfo(name = "strIngredient7") val strIngredient7: String,
    @ColumnInfo(name = "strIngredient8") val strIngredient8: String,
    @ColumnInfo(name = "strIngredient9") val strIngredient9: String,
    @ColumnInfo(name = "strIngredient10") val strIngredient10: String,
    @ColumnInfo(name = "strMeasure1") val strMeasure1: String,
    @ColumnInfo(name = "strMeasure2") val strMeasure2: String,
    @ColumnInfo(name = "strMeasure3") val strMeasure3: String,
    @ColumnInfo(name = "strMeasure4") val strMeasure4: String,
    @ColumnInfo(name = "strMeasure5") val strMeasure5: String,
    @ColumnInfo(name = "strMeasure6") val strMeasure6: String,
    @ColumnInfo(name = "strMeasure7") val strMeasure7: String,
    @ColumnInfo(name = "strMeasure8") val strMeasure8: String,
    @ColumnInfo(name = "strMeasure9") val strMeasure9: String,
    @ColumnInfo(name = "strMeasure10") val strMeasure10: String,
    @ColumnInfo(name = "strSource") val strSource: String,
    @ColumnInfo(name = "strImageSource") val strImageSource: String?,
    @ColumnInfo(name = "strCreativeCommonsConfirmed") val strCreativeCommonsConfirmed: Boolean?,
    @ColumnInfo(name = "dateModified") val dateModified: String?
) : Serializable{
    fun getMeasure(i: Int): String? {
        return when (i) {
            1 -> strMeasure1
            2 -> strMeasure2
            3 -> strMeasure3
            4 -> strMeasure4
            5 -> strMeasure5
            6 -> strMeasure6
            7 -> strMeasure7
            8 -> strMeasure8
            9 -> strMeasure9
            10 -> strMeasure10
            else -> null
        }
    }

    fun getIngredient(i: Int): String? {
        return when (i) {
            1 -> strIngredient1
            2 -> strIngredient2
            3 -> strIngredient3
            4 -> strIngredient4
            5 -> strIngredient5
            6 -> strIngredient6
            7 -> strIngredient7
            8 -> strIngredient8
            9 -> strIngredient9
            10 -> strIngredient10
            else -> null
        }
    }

}