package com.example.myrecipes.Repository

import com.example.myrecipes.model.database.UserSavedRecipes.UserSavedRecipes
import com.example.myrecipes.model.database.UserSavedRecipes.UserSavedRecipesDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class UserSavedRecipesRepositoryTest {

    private val fakeDao = FakeUserSavedRecipesDao()

     class FakeUserSavedRecipesDao : UserSavedRecipesDao {
        var savedRecipes = mutableListOf<UserSavedRecipes>()

        override suspend fun insert(savedRecipes: UserSavedRecipes) {
            this.savedRecipes.add(savedRecipes)
        }

        override suspend fun delete(savedRecipes: UserSavedRecipes) {
            this.savedRecipes.remove(savedRecipes)
        }

        override fun getSavedRecipeCount(userId: Long, recipeId: String): Int {
            return savedRecipes.count { it.userId == userId && it.recipeId == recipeId }
        }

         override suspend fun getRecipesRelationByUser(userId: Long): List<String>? {
             TODO("Not yet implemented")
         }
     }

    @Test
    fun `test add saved recipes`() = runTest {
        val savedRecipe = UserSavedRecipes(userId = 1, recipeId = "1")
        fakeDao.insert(savedRecipe)

        assertTrue(fakeDao.savedRecipes.contains(savedRecipe))
    }

    @Test
    fun `test remove saved recipes`() = runTest {
        val savedRecipe = UserSavedRecipes(userId = 1, recipeId = "1")
        fakeDao.insert(savedRecipe)
        fakeDao.delete(savedRecipe)

        assertTrue(fakeDao.savedRecipes.isEmpty())
    }

    @Test
    fun `test get saved recipe count`() = runTest {
        val savedRecipe = UserSavedRecipes(userId = 1, recipeId = "1")
        fakeDao.insert(savedRecipe)

        val count = fakeDao.getSavedRecipeCount(1, "1")
        assertEquals(1, count)
    }
}
