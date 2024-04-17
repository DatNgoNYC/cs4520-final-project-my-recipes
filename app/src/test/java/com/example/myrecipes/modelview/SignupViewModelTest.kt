package com.example.myrecipes.modelview

import android.app.Application
import com.example.myrecipes.model.database.User.User
import com.example.myrecipes.model.database.User.UserRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SignupViewModelTest {
    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var viewModel: SignupViewModel

    private lateinit var user: User

    @Before
    fun setUp() {
        viewModel = SignupViewModel(application, userRepository)
        user = User(
            username = "username", password = "password", email = "example@gmail.com"
        )
    }

    @Test
    fun `initial state`() {
        assertEquals("", viewModel.usernameText.value)
        assertEquals("", viewModel.emailText.value)
        assertEquals("", viewModel.passwordText.value)
        assertEquals("", viewModel.errorMessage.value)
    }

    @Test
    fun `test setUsername function`() {
        assertEquals("", viewModel.usernameText.value)
        viewModel.setUsernameText("username")
        assertEquals("username", viewModel.usernameText.value)
    }

    @Test
    fun `test setEmailText function`() {
        assertEquals("", viewModel.emailText.value)
        viewModel.setEmailText("example@gmail.com")
        assertEquals("example@gmail.com", viewModel.emailText.value)
    }

    @Test
    fun `test setPasswordText function`() {
        assertEquals("", viewModel.passwordText.value)
        viewModel.setPasswordText("password")
        assertEquals("password", viewModel.passwordText.value)
    }

    @Test
    fun `test isValidSignUp but blank email`() = runTest {
        viewModel.setEmailText("")
        assertEquals(false, viewModel.isValidSignUp())
        assertEquals("Please use a valid email to sign up.", viewModel.errorMessage.value)
    }

    @Test
    fun `test isValidSignUp but invalid email`() = runTest {
        viewModel.setEmailText("email")
        assertEquals(false, viewModel.isValidSignUp())
        assertEquals("Please use a valid email to sign up.", viewModel.errorMessage.value)
    }

    @Test
    fun `test isValidSignUp but blank name`() = runTest {
        viewModel.setEmailText("example@gmail.com")
        viewModel.setUsernameText("")
        assertEquals(false, viewModel.isValidSignUp())
        assertEquals("Username should be 3 to 16 characters.", viewModel.errorMessage.value)
    }

    @Test
    fun `test isValidSignUp but name is too long`() = runTest {
        viewModel.setEmailText("example@gmail.com")
        viewModel.setUsernameText("aaaaaaaaaaaaaaaaaaaaaaaaaa")
        assertEquals(false, viewModel.isValidSignUp())
        assertEquals("Username should be 3 to 16 characters.", viewModel.errorMessage.value)
    }

    @Test
    fun `test isValidSignUp but blank password`() = runTest {
        viewModel.setEmailText("example@gmail.com")
        viewModel.setUsernameText("username")
        viewModel.setPasswordText("")
        assertEquals(false, viewModel.isValidSignUp())
        assertEquals("Password should be at least 6 characters.", viewModel.errorMessage.value)
    }

    @Test
    fun `test isValidSignUp but password too short`() = runTest {
        viewModel.setEmailText("example@gmail.com")
        viewModel.setUsernameText("username")
        viewModel.setPasswordText("pass")
        assertEquals(false, viewModel.isValidSignUp())
        assertEquals("Password should be at least 6 characters.", viewModel.errorMessage.value)
    }

    @Test
    fun `test isValidSignUp but user already exists`() = runTest {
        `when`(userRepository.getUserByEmail(anyString())).thenReturn(user)
        viewModel.setEmailText("example@gmail.com")
        viewModel.setUsernameText("username")
        viewModel.setPasswordText("password")
        assertEquals(false, viewModel.isValidSignUp())
        assertEquals("User with the given email already exists!", viewModel.errorMessage.value)
    }

    @Test
    fun `test isValidSignUp returns true`() = runTest {
        `when`(userRepository.getUserByEmail("example@gmail.com")).thenReturn(null)
        viewModel.setEmailText("example@gmail.com")
        viewModel.setUsernameText("username")
        viewModel.setPasswordText("password")

        val userList = mutableListOf<User>()
        `when`(userRepository.insertUser(user)).then {
            userList.add(user)
        }

        // user logs in and inputs are cleared
        assertEquals(true, viewModel.isValidSignUp())
        assertEquals("", viewModel.emailText.value)
        assertEquals("", viewModel.usernameText.value)
        assertEquals("", viewModel.passwordText.value)
        assertEquals(true, userList.contains(user))
    }

    @Test
    fun `test error message clears after input interaction`() = runTest {
        assertEquals(false, viewModel.isValidSignUp())
        assertEquals("Please use a valid email to sign up.", viewModel.errorMessage.value)

        viewModel.setEmailText("example@gmail.com")
        assertEquals("", viewModel.errorMessage.value)
    }

    @Test
    fun `test clearAll resets all text`() = runTest {
        viewModel.setEmailText("a")
        viewModel.setUsernameText("username")
        viewModel.setPasswordText("password")
        viewModel.isValidSignUp()
        assertEquals("username", viewModel.usernameText.value)
        assertEquals("a", viewModel.emailText.value)
        assertEquals("password", viewModel.passwordText.value)
        assertEquals("Please use a valid email to sign up.", viewModel.errorMessage.value)

        viewModel.clearAll()
        assertEquals("", viewModel.usernameText.value)
        assertEquals("", viewModel.emailText.value)
        assertEquals("", viewModel.passwordText.value)
        assertEquals("", viewModel.errorMessage.value)
    }
}