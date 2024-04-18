package com.example.myrecipes.UI

import android.content.Context
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myrecipes.modelview.LoginViewModel
import com.example.myrecipes.modelview.SavedRecipesViewModel
import com.example.myrecipes.view.UI.Login
import com.example.myrecipes.R
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class LoginTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var navController: NavHostController = mock(NavHostController::class.java)

    private var loginViewModel: LoginViewModel = mock(LoginViewModel::class.java)

    private var savedRecipesViewModel: SavedRecipesViewModel = mock(SavedRecipesViewModel::class.java)

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            Login(
                viewModel = loginViewModel,
                uiState = LoginViewModel.UiState(),
                savedRecipesViewModel = savedRecipesViewModel,
                navController = navController
            )
        }
    }

    @Test
    fun testButtons() {
        val loginButton = composeTestRule.onNodeWithText(context.getString(R.string.login))
        loginButton.assertIsDisplayed()
        loginButton.assertHasClickAction()

        val backButton = composeTestRule.onNodeWithText(context.getString(R.string.back))
        backButton.assertIsDisplayed()
        backButton.assertHasClickAction()
    }

    @Test
    fun testInputLabelsVisible() {
        composeTestRule.onNodeWithText(context.getString(R.string.email)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.password)).assertIsDisplayed()
    }

    @Test
    fun testTextFieldInteractions() {
        val emailTextField = composeTestRule.onNodeWithText("enter email")
        val passwordTextField = composeTestRule.onNodeWithText("enter password")

        emailTextField.assertIsDisplayed()
        emailTextField.assertHasClickAction()

        passwordTextField.assertIsDisplayed()
        passwordTextField.assertHasClickAction()


        emailTextField.assertIsNotFocused()
        emailTextField.performClick()
        emailTextField.assertIsFocused()

        passwordTextField.assertIsNotFocused()
        passwordTextField.performClick()
        emailTextField.assertIsNotFocused()
        passwordTextField.assertIsFocused()
    }
}