import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.myrecipes.modelview.RecipesListViewModel
import com.example.myrecipes.modelview.SavedRecipesViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SaveRecipeButton(
    savedRecipesViewModel: SavedRecipesViewModel,
    recipesListViewModel: RecipesListViewModel,
    recipeId: String,
    user_id : Long
) {
    val viewModelScope = rememberCoroutineScope() // Create a local coroutine scope
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Launch a coroutine to fetch the saved status
        isSaved = savedRecipesViewModel.getSavedRecipe(user_id, recipeId)
    }

    Button(
        onClick = {
            viewModelScope.launch {
                if (isSaved) {
                    savedRecipesViewModel.removeSavedRecipes(user_id, recipeId)
                    val recipe = recipesListViewModel.getRecipeById(recipeId)
                    savedRecipesViewModel.removeRecipe(recipe)
                    isSaved = false
                } else {
                    savedRecipesViewModel.addSavedRecipes(user_id, recipeId)
                    val recipe = recipesListViewModel.getRecipeById(recipeId)
                    savedRecipesViewModel.addRecipe(recipe)
                    isSaved = true
                }
            }
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(if (isSaved) "Unsave Recipe" else "Save Recipe")
    }
}
