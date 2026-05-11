package com.example.hongguolite.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hongguolite.data.mock.MockCoverProvider
import com.example.hongguolite.data.mock.MockDramas
import com.example.hongguolite.data.model.Drama
import com.example.hongguolite.data.model.DramaBadge
import com.example.hongguolite.ui.theme.HongguoLiteTheme

@Composable
fun DramaCard(
    drama: Drama,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
    ) {
        DramaCover(
            drama = drama,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 12.dp)
        ) {
            Text(
                text = drama.title,
                color = Color(0xFF222222),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            DramaTagRow(tags = drama.tags)
        }
    }
}

@Composable
private fun DramaCover(
    drama: Drama,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(0.74f)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Image(
            painter = painterResource(id = MockCoverProvider.getPlaceholder(drama.id)),
            contentDescription = drama.title,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.56f)
                        )
                    )
                )
        )

        drama.badge?.let { badge ->
            DramaBadgeLabel(
                badge = badge,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )
        }

        DramaHeatLabel(
            heatCount = drama.heatCount,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
        )
    }
}

@Composable
private fun DramaBadgeLabel(
    badge: DramaBadge,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = when (badge) {
        DramaBadge.HotDrama -> Color(0xFFE95862)
        DramaBadge.NewDrama -> Color(0xFF38B885)
    }

    Text(
        text = badge.text,
        color = Color.White,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 7.dp, bottomEnd = 7.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
private fun DramaHeatLabel(
    heatCount: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "${heatCount}热度",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DramaTagRow(tags: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tags.take(3).forEachIndexed { index, tag ->
            if (index > 0) {
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = tag,
                color = Color(0xFF9B9B9B),
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 190)
@Composable
private fun DramaCardPreview() {
    HongguoLiteTheme(dynamicColor = false) {
        DramaCard(
            drama = MockDramas.theaterList.first(),
            onClick = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 190)
@Composable
private fun DramaCardWithBadgePreview() {
    HongguoLiteTheme(dynamicColor = false) {
        DramaCard(
            drama = MockDramas.theaterList[1],
            onClick = {},
            modifier = Modifier.padding(12.dp)
        )
    }
}
