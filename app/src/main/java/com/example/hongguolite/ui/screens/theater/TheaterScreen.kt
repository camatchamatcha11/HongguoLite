package com.example.hongguolite.ui.screens.theater

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hongguolite.R
import com.example.hongguolite.data.mock.MockDramas
import com.example.hongguolite.data.model.Drama
import com.example.hongguolite.data.model.theaterTabs
import com.example.hongguolite.ui.theme.HongguoLiteTheme

const val FIND_DRAMA_TAB_INDEX = 0

@Composable
fun TheaterScreen(
    onSearchBoxClick: () -> Unit,
    onRankClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TheaterViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val msgScreenshot = stringResource(id = R.string.theater_screenshot_unimplemented)
    val msgFilter = stringResource(id = R.string.theater_filter_unimplemented)
    val msgNewDrama = stringResource(id = R.string.theater_new_drama_unimplemented)
    val msgReservation = stringResource(id = R.string.theater_reservation_unimplemented)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(com.example.hongguolite.ui.theme.TheaterPageBackground)
    ) {
        TheaterTopBar(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            onSearchBoxClick = onSearchBoxClick,
            onScreenshotClick = {
                Toast.makeText(context, msgScreenshot, Toast.LENGTH_SHORT).show()
            },
            onFilterClick = {
                Toast.makeText(context, msgFilter, Toast.LENGTH_SHORT).show()
            },
            onRankClick = onRankClick,
            onNewDramaClick = {
                Toast.makeText(context, msgNewDrama, Toast.LENGTH_SHORT).show()
            },
            onReservationClick = {
                Toast.makeText(context, msgReservation, Toast.LENGTH_SHORT).show()
            },
        )

        when (selectedTabIndex) {
            FIND_DRAMA_TAB_INDEX -> {
                FindDramaContent(
                    dramas = uiState.dramas,
                    onDramaClick = { drama ->
                        showDramaPlaceholderToast(context, drama)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            else -> {
                TheaterTabPlaceholder(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun showDramaPlaceholderToast(
    context: android.content.Context,
    drama: Drama,
) {
    val message = context.getString(R.string.theater_drama_detail_unimplemented, drama.title)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
private fun TheaterTabPlaceholder(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
) {
    val tabLabel = theaterTabs.getOrNull(selectedTabIndex)?.label.orEmpty()
    val placeholderText = if (selectedTabIndex == FIND_DRAMA_TAB_INDEX) {
        stringResource(id = R.string.theater_find_drama_placeholder)
    } else {
        stringResource(id = R.string.theater_tab_unimplemented, tabLabel)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = placeholderText,
            color = com.example.hongguolite.ui.theme.TheaterPlaceholderTextGray,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 760)
@Composable
private fun TheaterScreenPreview() {
    HongguoLiteTheme(dynamicColor = false) {
        TheaterScreen(
            onSearchBoxClick = {},
            onRankClick = {},
        )
    }
}
