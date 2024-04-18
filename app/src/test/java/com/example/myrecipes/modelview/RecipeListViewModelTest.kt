package com.example.myrecipes.modelview

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.myrecipes.fakes.FakeRecipe
import com.example.myrecipes.model.database.Recipes.Recipe
import com.example.myrecipes.model.database.Recipes.RecipesRepository
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

// overrides the main dispatcher for launching coroutine scope
// https://stackoverflow.com/questions/58303961/kotlin-coroutine-unit-test-fails-with-module-with-the-main-dispatcher-had-faile
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RecipeListViewModelTest {
    // https://stackoverflow.com/questions/58057769/method-getmainlooper-in-android-os-looper-not-mocked-still-occuring-even-after-a
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var recipeRepository: RecipesRepository

    @Mock
    private lateinit var workManager: WorkManager

    private lateinit var viewModel: RecipesListViewModel

    @Before
    fun setUp() {
        `when`(application.applicationContext).thenReturn(mock(Context::class.java))

//        `when`(workManager.enqueueUniquePeriodicWork(anyString(), any(), any())).thenReturn()
        `when`(workManager.getWorkInfoByIdLiveData(any())).thenReturn(MutableLiveData<WorkInfo>())
        viewModel = RecipesListViewModel(application, workManager, recipeRepository)
    }

    @Test
    fun `initial state`() = runTest {
        TestCase.assertTrue(viewModel.recipes.value.isEmpty())
        TestCase.assertEquals(false, viewModel.error.value)
        TestCase.assertEquals(true, viewModel.loading.value)
        TestCase.assertEquals(null, viewModel.page.value)
    }

    @Test
    fun `fetch recipe`()= runTest {
        viewModel.setRecipes(listOf(FakeRecipe))

        val recipeReceived = viewModel.getRecipeById("12345")
        if (recipeReceived != null) {
            TestCase.assertEquals(recipeReceived.idMeal, FakeRecipe.idMeal)
            TestCase.assertEquals(recipeReceived.dateModified, FakeRecipe.dateModified)
            TestCase.assertEquals(recipeReceived.strCategory, FakeRecipe.strCategory)
            TestCase.assertEquals(recipeReceived.strImageSource, FakeRecipe.strImageSource)
        }
    }
}
