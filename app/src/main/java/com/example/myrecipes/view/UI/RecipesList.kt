package com.example.myrecipes.view.UI

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.myrecipes.modelview.RecipesListViewModel

@Composable
fun RecipesList(modelViewModel: RecipesListViewModel) {
    val productsState = modelViewModel.recipes.collectAsState()
    val loadingState = modelViewModel.loading.collectAsState()
    val errorState = modelViewModel.error.collectAsState()
    val pageState = modelViewModel.page.collectAsState()


    // Access the values
    val products = productsState.value
    val isLoading = loadingState.value
    val isError = errorState.value
}