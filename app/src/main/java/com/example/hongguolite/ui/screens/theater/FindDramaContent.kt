package com.example.hongguolite.ui.screens.theater

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hongguolite.data.mock.MockDramas
import com.example.hongguolite.data.model.Drama
import com.example.hongguolite.ui.components.DramaCard
import com.example.hongguolite.ui.theme.HongguoLiteTheme

@Composable
fun FindDramaContent(
    dramas: List<Drama>,
    onDramaClick: (Drama) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        items(
            items = dramas,
            key = { it.id }
        ) { drama ->
            DramaCard(
                drama = drama,
                onClick = { onDramaClick(drama) }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 720)
@Composable
private fun FindDramaContentPreview() {
    HongguoLiteTheme(dynamicColor = false) {
        FindDramaContent(
            dramas = MockDramas.theaterList,
            onDramaClick = {},
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
