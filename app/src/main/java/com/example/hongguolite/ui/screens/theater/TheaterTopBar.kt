package com.example.hongguolite.ui.screens.theater

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hongguolite.data.model.theaterTabs
import com.example.hongguolite.ui.theme.HongguoLiteTheme
import com.example.hongguolite.ui.theme.TheaterFilterBackground
import com.example.hongguolite.ui.theme.TheaterNewDramaBackground
import com.example.hongguolite.ui.theme.TheaterRankBackground
import com.example.hongguolite.ui.theme.TheaterReservationBackground
import com.example.hongguolite.R
import com.example.hongguolite.ui.theme.TheaterPageBackground

private val TheaterOrange = Color(0xFFFF7A1A)
private val TheaterTextPrimary = Color(0xFF1F1F1F)
private val TheaterTextSecondary = Color(0xFF8A8A8A)

/**
 * 剧场顶部栏 Composable，包含搜索框、频道Tab、快捷入口。
 * @param selectedTabIndex 当前选中的Tab索引
 * @param onTabSelected Tab切换回调
 * @param onSearchBoxClick 搜索框点击回调
 * @param onScreenshotClick 截图识别短剧点击回调
 * @param onFilterClick 筛选入口点击回调
 * @param onRankClick 排行榜入口点击回调
 * @param onNewDramaClick 新剧入口点击回调
 * @param onReservationClick 预约入口点击回调
 * @param modifier 外部修饰符
 */
@Composable
fun TheaterTopBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onSearchBoxClick: () -> Unit,
    onScreenshotClick: () -> Unit,
    onFilterClick: () -> Unit,
    onRankClick: () -> Unit,
    onNewDramaClick: () -> Unit,
    onReservationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFEAFBFA), TheaterPageBackground),
                )
            )
            .padding(top = 10.dp, bottom = 8.dp)
    ) {
        // 搜索行
        TheaterSearchRow(
            onSearchBoxClick = onSearchBoxClick,
            onScreenshotClick = onScreenshotClick,
        )

        // 频道Tab
        TheaterChannelTabs(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = onTabSelected,
        )

        // 快捷入口行
        TheaterQuickEntryRow(
            onFilterClick = onFilterClick,
            onRankClick = onRankClick,
            onNewDramaClick = onNewDramaClick,
            onReservationClick = onReservationClick,
        )
    }
}

/**
 * 剧场顶部搜索行，包含搜索框和截图识别入口。
 * @param onSearchBoxClick 搜索框点击回调
 * @param onScreenshotClick 截图识别短剧点击回调
 */
@Composable
private fun TheaterSearchRow(
    onSearchBoxClick: () -> Unit,
    onScreenshotClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .height(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.96f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左侧搜索框
        Row(
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .clickable(onClick = onSearchBoxClick)
                .padding(start = 14.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                tint = Color(0xFF9C9C9C),
                modifier = Modifier.size(23.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "我在精神病院学斩神",
                color = Color(0xFF8C8C8C),
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        // 分割线
        Box(
            modifier = Modifier
                .height(24.dp)
                .width(1.dp)
                .background(Color(0xFFE8E8E8))
        )

        // 右侧截图识别入口
        Row(
            modifier = Modifier
                .height(46.dp)
                .clickable(onClick = onScreenshotClick)
                .padding(start = 12.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "截图识别短剧",
                tint = Color(0xFF8C8C8C),
                modifier = Modifier.size(23.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "截图识别短剧",
                color = Color(0xFF8C8C8C),
                fontSize = 16.sp,
                maxLines = 1
            )
        }
    }
}

/**
 * 剧场频道Tab栏，支持横向滚动。
 * @param selectedTabIndex 当前选中的Tab索引
 * @param onTabSelected Tab切换回调
 */
@Composable
private fun TheaterChannelTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    val safeSelectedIndex = selectedTabIndex.coerceIn(theaterTabs.indices)

    ScrollableTabRow(
        selectedTabIndex = safeSelectedIndex,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        contentColor = TheaterTextPrimary,
        edgePadding = 14.dp,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[safeSelectedIndex])
                    .padding(horizontal = 20.dp),
                height = 3.dp,
                color = TheaterOrange
            )
        }
    ) {
        // 遍历所有Tab，渲染Tab项
        theaterTabs.forEachIndexed { index, tab ->
            val selected = index == selectedTabIndex
            Tab(
                selected = selected,
                onClick = { onTabSelected(index) },
                selectedContentColor = TheaterTextPrimary,
                unselectedContentColor = TheaterTextSecondary,
                text = {
                    Text(
                        text = tab.label,
                        fontSize = 26.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1
                    )
                }
            )
        }
    }
}

/**
 * 剧场顶部快捷入口行，包含筛选、排行榜、新剧、预约。
 * @param onFilterClick 筛选点击回调
 * @param onRankClick 排行榜点击回调
 * @param onNewDramaClick 新剧点击回调
 * @param onReservationClick 预约点击回调
 */
@Composable
private fun TheaterQuickEntryRow(
    onFilterClick: () -> Unit,
    onRankClick: () -> Unit,
    onNewDramaClick: () -> Unit,
    onReservationClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 筛选、排行榜、新剧、预约四个入口
        TheaterQuickEntry(Icons.Default.FilterList, "筛选", Color(0xFF7D63F3), onFilterClick, Modifier.weight(1f))
        TheaterQuickEntry(Icons.Default.LocalFireDepartment, "排行榜", Color(0xFFFF8A24), onRankClick, Modifier.weight(1f))
        TheaterQuickEntry(Icons.Default.PlayArrow, "新剧", Color(0xFF48C7C5), onNewDramaClick, Modifier.weight(1f))
        TheaterQuickEntry(Icons.Default.Apps, "预约", Color(0xFFFFBD4A), onReservationClick, Modifier.weight(1f))
    }
}

/**
 * 快捷入口单元组件，带图标和文字。
 * @param icon 图标
 * @param label 文字标签
 * @param iconBackground 图标背景色
 * @param onClick 点击回调
 * @param modifier 外部修饰符
 */
@Composable
private fun TheaterQuickEntry(
    icon: ImageVector,
    label: String,
    iconBackground: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // 图标区域
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        // 文字标签
        Text(
            text = label,
            color = TheaterTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 剧场顶部栏预览
 */
@Preview(showBackground = true, widthDp = 393)
@Composable
private fun TheaterTopBarPreview() {
    HongguoLiteTheme(dynamicColor = false) {
        TheaterTopBar(
            selectedTabIndex = 0,
            onTabSelected = {},
            onSearchBoxClick = {},
            onScreenshotClick = {},
            onFilterClick = {},
            onRankClick = {},
            onNewDramaClick = {},
            onReservationClick = {},
        )
    }
}
