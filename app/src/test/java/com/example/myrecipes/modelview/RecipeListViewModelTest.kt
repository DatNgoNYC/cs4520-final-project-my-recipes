//package com.example.myrecipes.modelview
//
//import android.app.Application
//import android.content.Context
//import androidx.work.WorkManager
//import com.example.myrecipes.model.database.Recipes.Recipe
//import com.example.myrecipes.model.database.Recipes.RecipesRepository
//import junit.framework.TestCase
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.`when`
//import org.mockito.junit.MockitoJUnitRunner
//
//
//@OptIn(ExperimentalCoroutinesApi::class)
//@RunWith(MockitoJUnitRunner::class)
//class RecipeListViewModelTest {
//    @Mock
//    private lateinit var application: Application
//
//    @Mock
//    private lateinit var recipeRepository: RecipesRepository
//
//    @Mock
//    private lateinit var workManager: WorkManager
//
//    private lateinit var viewModel: RecipesListViewModel
//
//    private lateinit var recipe: Recipe
//
//    @Before
//    fun setUp() {
//        `when`(application.applicationContext).thenReturn(mock(Context::class.java))
//
//        workManager = WorkManager.getInstance(application.applicationContext)
//        viewModel = RecipesListViewModel(application, workManager, recipeRepository)
//
//        recipe = Recipe(
//            idMeal = "12345",
//            strMeal = "Sample Recipe Name",
//            strDrinkAlternate = null, // Nullable field
//            strCategory = "Dessert",
//            strArea = "International",
//            strInstructions = "Mix all ingredients and bake for 45 minutes at 350 degrees.",
//            strMealThumb = "file:///android_res/drawable/your_placeholder_image.png",
//            strTags = "Easy, Quick",
//            strYoutube = "https://youtube.com/samplevideo",
//            strIngredient1 = "Flour",
//            strIngredient2 = "Sugar",
//            strIngredient3 = "Eggs",
//            strIngredient4 = "Butter",
//            strIngredient5 = "Baking Soda",
//            strIngredient6 = "Salt",
//            strIngredient7 = "Vanilla Extract",
//            strIngredient8 = "Milk",
//            strIngredient9 = "Cocoa Powder",
//            strIngredient10 = "Chocolate Chips",
//            strMeasure1 = "1 Cup",
//            strMeasure2 = "200g",
//            strMeasure3 = "3 Large",
//            strMeasure4 = "100g",
//            strMeasure5 = "1 Tsp",
//            strMeasure6 = "1 Tsp",
//            strMeasure7 = "2 Tsp",
//            strMeasure8 = "200ml",
//            strMeasure9 = "50g",
//            strMeasure10 = "100g",
//            strSource = "https://www.samplewebsite.com/recipes/sample-recipe-name",
//            strImageSource = null, // Nullable field
//            strCreativeCommonsConfirmed = null, // Nullable field
//            dateModified = null // Nullable field
//        )
//    }
//
//    @Test
//    fun `initial state`() = runTest {
//        TestCase.assertTrue(viewModel.recipes.value.isEmpty())
//        TestCase.assertEquals(false, viewModel.error.value)
//        TestCase.assertEquals(false, viewModel.loading.value)
//        TestCase.assertEquals(0, viewModel.page.value)
//    }
//
//    @Test
//    fun `fetch recipe`()= runTest {
//        viewModel.setRecipes(listOf(recipe))
//
//        val recipeReceived = viewModel.getRecipeById("12345")
//        if (recipeReceived != null) {
//            TestCase.assertEquals(recipeReceived.idMeal, recipe.idMeal)
//            TestCase.assertEquals(recipeReceived.dateModified, recipe.dateModified)
//            TestCase.assertEquals(recipeReceived.strCategory, recipe.strCategory)
//            TestCase.assertEquals(recipeReceived.strImageSource, recipe.strImageSource)
//        }
//    }
//}
