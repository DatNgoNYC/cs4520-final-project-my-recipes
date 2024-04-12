package com.example.myrecipes.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myrecipes.model.database.Recipes.Recipe
import com.example.myrecipes.model.database.Recipes.RecipesDao
import com.example.myrecipes.model.database.User.User
import com.example.myrecipes.model.database.User.UserDao

@Database(entities = [Recipe::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun RecipeDao(): RecipesDao
    abstract fun UserDao(): UserDao

    // initialized the database project
    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                try {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .addCallback(roomCallback)
                        .build()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return instance!!
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }

}