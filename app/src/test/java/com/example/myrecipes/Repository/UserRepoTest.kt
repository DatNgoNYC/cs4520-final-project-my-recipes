package com.example.myrecipes.Repository

import com.example.myrecipes.model.database.User.User
import com.example.myrecipes.model.database.User.UserDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class UserRepoTest {

    private val fakeDao = FakeUserDao()

    class FakeUserDao : UserDao {
        var fakeUser = mutableListOf<User>()

        override suspend fun insert(user: User) {
            fakeUser.add(user)
        }

        override suspend fun getUserById(userId: Long): User? {
            return fakeUser.find { it.userId == userId }
        }

        override suspend fun getUserByEmail(email: String): User? {
            return fakeUser.find { it.email == email }
        }
    }

    @Test
    fun `test insert user`() = runTest {
        val user = User(username = "username", password = "password", email = "example@gmail.com")
        fakeDao.insert(user)

        assertTrue(fakeDao.fakeUser.contains(user))
    }

    @Test
    fun `test get user by id`() = runTest {
        val user = User(userId = 1, username = "username", password = "password", email = "example@gmail.com")
        fakeDao.insert(user)

        val retrievedUser = fakeDao.getUserById(1)
        assertEquals(user, retrievedUser)
    }

    @Test
    fun `test get user by email`() = runTest {
        val user = User(userId = 1, username = "username", password = "password", email = "example@gmail.com")
        fakeDao.insert(user)

        val retrievedUser = fakeDao.getUserByEmail("example@gmail.com")
        assertEquals(user, retrievedUser)
    }
}
