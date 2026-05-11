package com.example.hongguolite.ui.screens

import android.R.attr.direction
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.KeyboardArrowRight

import androidx.compose.material.icons.filled.Star
import com.example.hongguolite.R
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun GenreCapsule(text: String) {
    // 类型胶囊：灰色半透明背景 + 圆角 + 小字号文字
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White.copy(alpha = 0.22f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp
        )
    }
}

@Composable
fun RightActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    contentDescription: String
) {
    // 单个右侧操作按钮：上面是图标，下面是数字
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(38.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit  // 搜索按钮点击回调，将来跳转搜索页
) {
    val context = LocalContext.current

    // Box：层叠布局，子元素从下到上叠加
    Box(modifier = Modifier.fillMaxSize()) {

        // 第 1 层：全屏背景图
        Image(
            painter = painterResource(id = R.drawable.home_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop  // 等比缩放并裁剪，避免图片变形
        )

        // 第 2 层：顶部图标栏（左汉堡 + 右搜索）
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .align(Alignment.TopCenter),  // 把这个 Row 顶到 Box 顶部
            horizontalArrangement = Arrangement.SpaceBetween  // 左右分开
        ) {
            IconButton(onClick = {
                Toast.makeText(context, "侧边栏未实现", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "菜单",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }

            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }
        }

        // 第 3 层：右侧悬浮按钮列（子任务 1.5）
        // 这里不再使用 CenterEnd，因为真实短剧 App 的操作按钮整体偏下
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 14.dp, bottom = 90.dp),
//                .background(Color.Red.copy(alpha = 0.3f)),   // ← 临时染色,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            RightActionButton(
                icon = Icons.Default.Star,
                text = "40.2万",
                contentDescription = "收藏"
            )

            RightActionButton(
                icon = Icons.Default.ChatBubbleOutline,
                text = "2221",
                contentDescription = "评论"
            )

            RightActionButton(
                icon = Icons.Default.Favorite,
                text = "14.3万",
                contentDescription = "点赞"
            )

            RightActionButton(
                icon = Icons.Default.Share,
                text = "1万",
                contentDescription = "分享"
            )
        }

        // 第 4 层：底部信息区（子任务 1.6）
        // 整体贴在 Box 底部，但 bottom 留一些距离，让它不要挤到 BottomNavigation
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
        ) {
            // 信息文字区域
            // 右侧多留 88dp，是为了避开右侧悬浮按钮列
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 88.dp)
            ) {
                // 第 1 行：爆剧标签 + 剧名
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 红色小标签：【爆剧】
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFFE94B4B))
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "爆剧",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // 剧名
                    // weight(1f)：让 Text 占用 Row 中剩余空间，超出后才能正确省略
                    Text(
                        text = "全球冰封：开局手握百…",
                        color = Color.White,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 第 2 行：类型胶囊
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GenreCapsule("科幻末世")

                    Spacer(modifier = Modifier.width(8.dp))

                    GenreCapsule("都市脑洞")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 第 3 行：简介
                Text(
                    text = "第1集 | 极寒末日降临，全球九成…",
                    color = Color.White,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 第 4 行：“观看完整短剧”卡片
            // 这里不放在上面的信息文字区域里，是因为卡片本身应该接近全宽
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.55f))
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 卡片左侧：圆形播放按钮 + 文字
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 用 Material 自带的播放图标，不需要额外找素材
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "观看完整短剧 · 全75集",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // 卡片右侧：箭头
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "去观看",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun HomeScreenPreview() {
    com.example.hongguolite.ui.theme.HongguoLiteTheme(dynamicColor = false) {
        HomeScreen(
            onSearchClick = {}
        )
    }
}
