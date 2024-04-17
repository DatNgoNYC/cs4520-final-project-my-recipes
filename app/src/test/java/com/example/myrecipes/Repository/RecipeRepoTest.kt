package com.example.myrecipes.Repository

import com.example.myrecipes.model.database.Recipes.Recipe
import com.example.myrecipes.model.database.Recipes.RecipesDao
import com.example.myrecipes.model.database.User.User
import com.example.myrecipes.model.database.UserSavedRecipes.UserSavedRecipes
import com.example.myrecipes.modelview.SavedRecipesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RecipesRepositoryTest {

    private val fakeDao = FakeRecipesDao()

    class FakeRecipesDao : RecipesDao {
        var fakeRecipes = mutableListOf<Recipe>()

        override suspend fun getRecipesByPage(pageSize: Int, offset: Int): List<Recipe> {
            return fakeRecipes.slice(offset until offset + pageSize)
        }

        override suspend fun insert(recipe: Recipe) {
            fakeRecipes.add(recipe)
        }

        override suspend fun insertAll(products: List<Recipe>) {
            TODO("Not yet implemented")
        }

        override suspend fun getRecipeByName(recipeName: String): Recipe? {
            return fakeRecipes.find { it.strMeal == recipeName }
        }
    }

    private lateinit var recipe: Recipe

    @Before
    fun setUp() {

        recipe = Recipe(
            idMeal = "12345",
            strMeal = "Sample Recipe Name",
            strDrinkAlternate = null, // Nullable field
            strCategory = "Dessert",
            strArea = "International",
            strInstructions = "Mix all ingredients and bake for 45 minutes at 350 degrees.",
            strMealThumb = "file:///android_res/drawable/your_placeholder_image.png",
            strTags = "Easy, Quick",
            strYoutube = "https://youtube.com/samplevideo",
            strIngredient1 = "Flour",
            strIngredient2 = "Sugar",
            strIngredient3 = "Eggs",
            strIngredient4 = "Butter",
            strIngredient5 = "Baking Soda",
            strIngredient6 = "Salt",
            strIngredient7 = "Vanilla Extract",
            strIngredient8 = "Milk",
            strIngredient9 = "Cocoa Powder",
            strIngredient10 = "Chocolate Chips",
            strMeasure1 = "1 Cup",
            strMeasure2 = "200g",
            strMeasure3 = "3 Large",
            strMeasure4 = "100g",
            strMeasure5 = "1 Tsp",
            strMeasure6 = "1 Tsp",
            strMeasure7 = "2 Tsp",
            strMeasure8 = "200ml",
            strMeasure9 = "50g",
            strMeasure10 = "100g",
            strSource = "https://www.samplewebsite.com/recipes/sample-recipe-name",
            strImageSource = null, // Nullable field
            strCreativeCommonsConfirmed = null, // Nullable field
            dateModified = null // Nullable field
        )


    }

    @Test
    fun `test insert recipe`() = runTest {
        fakeDao.insert(recipe)

        assertTrue(fakeDao.fakeRecipes.contains(recipe))
    }

    @Test
    fun `test get recipe by name`() = runTest {
        fakeDao.insert(recipe)

        val retrievedRecipe = fakeDao.getRecipeByName("Sample Recipe Name")
        assertEquals(recipe, retrievedRecipe)
    }
}
