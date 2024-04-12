package com.example.myrecipes.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import androidx.work.WorkManager
import com.example.myrecipes.view.navigation.AppNavHost

class MainActivity : AppCompatActivity(){
    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(applicationContext)

        setContent {
            AppNavHost(context = this, navController = rememberNavController(), application = application, workManager = workManager)
        }
    }

}