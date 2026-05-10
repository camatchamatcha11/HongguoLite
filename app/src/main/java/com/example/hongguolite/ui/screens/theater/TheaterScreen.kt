package com.example.hongguolite.ui.screens.theater

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hongguolite.data.model.theaterTabs
import com.example.hongguolite.ui.theme.HongguoLiteTheme

@Composable
fun TheaterScreen(
    onSearchBoxClick: () -> Unit,
    onRankClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        TheaterTopBar(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            onSearchBoxClick = onSearchBoxClick,
            onScreenshotClick = {
                Toast.makeText(context, "截图识别短剧暂未实现", Toast.LENGTH_SHORT).show()
            },
            onFilterClick = {
                Toast.makeText(context, "筛选暂未实现", Toast.LENGTH_SHORT).show()
            },
            onRankClick = onRankClick,
            onNewDramaClick = {
                Toast.makeText(context, "新剧筛选暂未实现", Toast.LENGTH_SHORT).show()
            },
            onReservationClick = {
                Toast.makeText(context, "预约暂未实现", Toast.LENGTH_SHORT).show()
            },
        )

        TheaterTabPlaceholder(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TheaterTabPlaceholder(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
) {
    val tabLabel = theaterTabs.getOrNull(selectedTabIndex)?.label.orEmpty()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = if (selectedTabIndex == 0) {
                "找剧内容将在子任务 2.4 接入"
            } else {
                "$tabLabel 内容暂未实现"
            },
            color = Color(0xFF999999),
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
