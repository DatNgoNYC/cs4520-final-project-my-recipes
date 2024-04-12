package com.example.myrecipes.view.UI

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.myrecipes.modelview.RecipesListViewModel

@Composable
fun RecipeDetail(modelViewModel: RecipesListViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
//        Image(
//            painter = painterResource(),
//            contentDescription = null,
//            modifier = Modifier
//                .width(100.dp)
//                .height(100.dp)
//        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {

        }
    }

}


