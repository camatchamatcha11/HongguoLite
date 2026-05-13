package com.example.hongguolite.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hongguolite.ui.theme.HongguoLiteTheme
import com.example.hongguolite.ui.theme.HongguoRed

/**
 * Temporary Module 3.1 search destination.
 *
 * Later subtasks will replace the placeholder body with pre-search, suggesting, and
 * result states. Keeping this as a real route now lets Home/Theater navigation be
 * tested before the full search UI exists.
 */
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // The search page owns its own back affordance because it is not part of the
            // bottom-tab navigation set.
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "\u8fd4\u56de",
                    tint = Color(0xFF222222),
                )
            }

            // Static shell matching the later search top bar shape. Subtasks 3.4-3.6 will
            // wire this area to SearchViewModel state and text input events.
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF999999),
                    )
                    Text(
                        text = "\u641c\u7d22\u77ed\u5267",
                        color = Color(0xFF999999),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }

            Text(
                text = "\u641c\u7d22",
                color = HongguoRed,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        Spacer(modifier = Modifier.height(120.dp))

        Text(
            text = "\u641c\u7d22\u9875\u5360\u4f4d",
            color = Color(0xFF666666),
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun SearchScreenPreview() {
    HongguoLiteTheme(dynamicColor = false) {
        SearchScreen(onBackClick = {})
    }
}
