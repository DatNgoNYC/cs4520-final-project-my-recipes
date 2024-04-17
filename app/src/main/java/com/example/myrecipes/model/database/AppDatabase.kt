package com.example.myrecipes.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myrecipes.model.database.Recipes.Recipe
import com.example.myrecipes.model.database.Recipes.RecipesDao
import com.example.myrecipes.model.database.User.User
import com.example.myrecipes.model.database.User.UserDao
import com.example.myrecipes.model.database.UserSavedRecipes.UserSavedRecipes
import com.example.myrecipes.model.database.UserSavedRecipes.UserSavedRecipesDao

@Database(entities = [Recipe::class, User::class, UserSavedRecipes::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun RecipeDao(): RecipesDao
    abstract fun UserDao(): UserDao
    abstract fun UserSavedRecipesDao(): UserSavedRecipesDao

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
                        .addMigrations(MIGRATION_2_3)
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

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create a temporary table with the new schema
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `user_saved_recipes_temp` (" +
                            "`user_id` INTEGER NOT NULL, " +
                            "`recipe_Id` TEXT NOT NULL, " +
                            "PRIMARY KEY(`user_id`, `recipe_Id`))"
                )
                // Copy the data from the existing table to the temporary table
                database.execSQL("INSERT INTO `user_saved_recipes_temp` (`user_id`, `recipe_Id`) SELECT `user_id`, `recipe_Id` FROM `user_saved_recipes`")
                // Drop the existing table
                database.execSQL("DROP TABLE `user_saved_recipes`")
                // Rename the temporary table to the original table name
                database.execSQL("ALTER TABLE `user_saved_recipes_temp` RENAME TO `user_saved_recipes`")
            }
        }
    }
}
