package com.example.myrecipes.modelview

import android.app.Application
import com.example.myrecipes.fakes.FakeRecipe
import com.example.myrecipes.model.database.Recipes.Recipe
import com.example.myrecipes.model.database.User.User
import com.example.myrecipes.model.database.User.UserRepository
import com.example.myrecipes.model.database.UserSavedRecipes.UserSavedRecipes
import com.example.myrecipes.model.database.UserSavedRecipes.UserSavedRecipesRepository
import com.example.myrecipes.view.navigation.NavigationItem
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.lenient
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SavedRecipesModelTest {
    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var savedRecipesRepository: UserSavedRecipesRepository

    private lateinit var savedRecipesListViewModel: SavedRecipesViewModel

    private lateinit var user: User

    private lateinit var savedRecipe: UserSavedRecipes

    @Before
    fun setUp() {
        savedRecipesListViewModel = SavedRecipesViewModel(application, savedRecipesRepository)
        user = User(
            username = "username", password = "password", email = "example@gmail.com"
        )

        savedRecipe = UserSavedRecipes(1L, "123")
    }

    @Test
    fun `initial state`() {
        // For recipes
        TestCase.assertTrue(savedRecipesListViewModel.recipes.value.isEmpty())
        // For user_id
        TestCase.assertEquals(0L, savedRecipesListViewModel.user_id.value)
    }

    @Test
    fun `test set updateUser_Id function`() {
        TestCase.assertEquals(0L, savedRecipesListViewModel.user_id.value)
        savedRecipesListViewModel.updateUserId(10)
        TestCase.assertEquals(10, savedRecipesListViewModel.user_id.value)
    }

    @Test
    fun `test set add Recipe function`() = runTest {
        TestCase.assertEquals(0, savedRecipesListViewModel.recipes.value.size)

        // Call the function under test
        savedRecipesListViewModel.addRecipe(FakeRecipe)

        // Verify the size of savedRecipeList
        TestCase.assertEquals(1, savedRecipesListViewModel.recipes.value.size)
    }

    @Test
    fun `test set remove Recipe function`() = runTest {
        TestCase.assertEquals(0, savedRecipesListViewModel.recipes.value.size)

        // Call the function under test
        savedRecipesListViewModel.addRecipe(FakeRecipe)

        // Verify the size of savedRecipeList
        TestCase.assertEquals(1, savedRecipesListViewModel.recipes.value.size)

        savedRecipesListViewModel.removeRecipe(FakeRecipe)

        TestCase.assertEquals(0, savedRecipesListViewModel.recipes.value.size)


    }

    @Test
    fun `test set add saved Recipe function`() = runTest {
        val user_id = 1L
        val recipe_id = "123"
        val recipe = UserSavedRecipes(user_id, recipe_id)

        // Call the function
        savedRecipesListViewModel.addSavedRecipes(user_id, recipe_id)

        // Await the completion of the function
        delay(100) // Adjust the delay time as needed based on the asynchronous behavior

        // Verify that repository.addSavedRecipes was called with the correct recipe
        verify(savedRecipesRepository).addSavedRecipes(recipe)
    }

    @Test
    fun `test set remove saved Recipe function`() = runTest {
        val user_id = 1L
        val recipe_id = "123"
        val recipe = UserSavedRecipes(user_id, recipe_id)

        // Call the function
        savedRecipesListViewModel.removeSavedRecipes(user_id, recipe_id)

        // Await the completion of the function
        delay(100) // Adjust the delay time as needed based on the asynchronous behavior

        // Verify that repository.addSavedRecipes was called with the correct recipe
        verify(savedRecipesRepository).removeSavedRecipes(recipe)
    }
    @Test
    fun `test get saved recipe to true`() = runTest {
        // Mock the behavior of savedRecipesRepository.getSavedRecipe(savedRecipe)
        `when`(savedRecipesRepository.getSavedRecipe(savedRecipe)).thenReturn(1)

        // Call the function under test
        val found = savedRecipesListViewModel.getSavedRecipe(savedRecipe.userId, savedRecipe.recipeId)

        // Assert that the recipe is found (i.e., not null)
        TestCase.assertNotNull(found)

        // Assert that the found recipe has the expected userId and recipeId
        TestCase.assertEquals(true, found)
    }

    @Test
    fun `test get saved recipe to false`() = runTest {
        // Mock the behavior of savedRecipesRepository.getSavedRecipe(savedRecipe)
        `when`(savedRecipesRepository.getSavedRecipe(savedRecipe)).thenReturn(0)

        // Call the function under test
        val found = savedRecipesListViewModel.getSavedRecipe(savedRecipe.userId, savedRecipe.recipeId)

        // Assert that the recipe is found (i.e., not null)
        TestCase.assertNotNull(found)

        // Assert that the found recipe has the expected userId and recipeId
        TestCase.assertEquals(false, found)
    }

}