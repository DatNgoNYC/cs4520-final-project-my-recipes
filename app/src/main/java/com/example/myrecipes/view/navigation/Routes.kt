package com.example.myrecipes.view.navigation

enum class Screen {
    HOME_SCREEN,
    LOGIN,
    SIGNUP,
    RECIPE_LIST,
    RECIPE_DETAIL
}
sealed class NavigationItem(val route: String) {

    object Home : NavigationItem(Screen.HOME_SCREEN.name)
    object Login : NavigationItem(Screen.LOGIN.name)
    object Signup : NavigationItem(Screen.SIGNUP.name)
    object RecipeList : NavigationItem(Screen.RECIPE_LIST.name)
    object RecipeDetail : NavigationItem(Screen.RECIPE_DETAIL.name)

}