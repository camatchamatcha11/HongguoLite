# Module 2：剧场页（剧场 Tab + DramaCard 组件）

> **预估时间**：1.5 - 2 天
> **核心目标**：实现剧场页 UI 骨架 + 双列卡片瀑布流，打通"剧场搜索框 → 搜索前置页"入口
> **学习重点**：可滚动 Tab、双列网格、Mock 数据建模、可复用组件设计

---

## 子任务总览

| 子任务 | 内容 | 强制 check-in |
|---|---|---|
| 2.1 | 数据建模 + Mock 数据准备 | ❌ |
| 2.2 | 剧场页顶部：搜索框 + 一级 Tab + 4 个圆形入口 | ✅（必须发顶部代码 + 真机截图） |
| 2.3 | DramaCard 组件设计与实现 | ✅（必须发 DramaCard.kt + 接口设计理由） |
| 2.4 | 找剧 Tab 内容：双列卡片瀑布流 | ❌ |
| 2.5 | 联调：剧场 → 搜索前置页占位跳转 + Git 提交 | ✅（整体演示 + 当场改需求） |

## 子任务 2.1：数据建模 + Mock 数据

### 做什么

定义剧目数据模型，准备 30 条 Mock 数据。这是后面所有列表展示的数据基础。

### 知识点

**Java 类比**：你在 Java 里写 POJO 要：

```java
public class Drama {
    private String id;
    private String title;
    // 手写 getter/setter/equals/hashCode/toString...
}
```

Kotlin 一行搞定：

```kotlin
data class Drama(
    val id: String,
    val title: String,
)
```

`data class` 自动生成 `equals` / `hashCode` / `toString` / `copy` / `componentN`。

### 步骤 1：定义 Drama 数据模型

新建文件 `app/src/main/java/com/example/hongguolite/data/model/Drama.kt`：

```kotlin
package com.example.hongguolite.data.model

/**
 * 剧目数据模型
 *
 * 思考题（自己想，不用回答）：
 * 1. 为什么用 String 当 id 而不是 Int / Long？（真实业务里的 ID 不一定永远是纯数字，id 通常不是拿来做数学计算的，这些场景只要求它唯一、稳定、可传递，不要求它能加减乘除。）
 * 2. 为什么 coverUrl 是 String 而不是直接的 Int 资源 ID？
 */
data class Drama(
    val id: String,           // 剧目唯一标识
    val title: String,        // 剧名（如"全球冰封：开局手握百亿物资"）
    val coverUrl: String,     // 封面图 URL（Module 5 用，本期暂用本地图占位）
    val heatCount: String,    // 热度（如 "4254万"）
    val tags: List<String>,   // 类型标签（如 ["科幻末世", "都市脑洞"]）
    val badge: DramaBadge? = null  // 角标（爆剧/新剧/null）
)

/**
 * 角标枚举
 *
 * sealed class 是 Kotlin 特色，比 enum 更灵活：
 * - enum 每项是固定值
 * - sealed class 每项可以是不同类型/带不同参数
 *
 * 这里其实用 enum 也够用，但我故意用 sealed class 让你提前接触。
 * Module 4 状态管理时会大量用到。
 */
sealed class DramaBadge(val text: String, val colorHex: Long) {
    object HotDrama : DramaBadge("爆剧", 0xFFE6303C)   // 红色
    object NewDrama : DramaBadge("新剧", 0xFF34C759)   // 绿色
}
```

### 步骤 2：准备 Mock 数据

新建文件 `app/src/main/java/com/example/hongguolite/data/mock/MockDramas.kt`：

```kotlin
package com.example.hongguolite.data.mock

import com.example.hongguolite.data.model.Drama
import com.example.hongguolite.data.model.DramaBadge

/**
 * Mock 剧目数据。
 *
 * 现阶段所有数据写死在这里。Module 5 接入网络后，
 * 这个文件会被 Repository 的 Mock 实现替代，但数据本身保留。
 */
object MockDramas {

    val theaterList: List<Drama> = listOf(
        Drama(
            id = "drama_001",
            title = "这些房子不正常",
            coverUrl = "",  // Module 5 替换为远程 URL
            heatCount = "3741万",
            tags = listOf("悬疑", "都市"),
            badge = null
        ),
        Drama(
            id = "drama_002",
            title = "长安夜行录",
            coverUrl = "",
            heatCount = "4254万",
            tags = listOf("古装", "悬疑推理"),
            badge = DramaBadge.NewDrama
        ),
        Drama(
            id = "drama_003",
            title = "过年回家，和三个精神小妹挤大巴",
            coverUrl = "",
            heatCount = "4261万",
            tags = listOf("都市", "搞笑"),
            badge = DramaBadge.HotDrama
        ),
        Drama(
            id = "drama_004",
            title = "我的26岁女房客",
            coverUrl = "",
            heatCount = "3924万",
            tags = listOf("都市爱情", "总裁"),
            badge = null
        ),
        Drama(
            id = "drama_005",
            title = "全球冰封：开局手握百亿物资",
            coverUrl = "",
            heatCount = "4004万",
            tags = listOf("科幻末世", "都市脑洞"),
            badge = DramaBadge.HotDrama
        ),
        // TODO：你需要继续补到 30 条
        // 提示：直接抄改上面格式，剧名和 tags 从你截图的红果 App 里挑
        // 不要用"剧目1、剧目2"这种敷衍的占位名，要像真实剧名（夸张点也没事，红果原本就这风格）
    )
}
```

### 步骤 3：占位封面图

在 `app/src/main/res/drawable/` 下放 5 张图片，命名：

- `cover_placeholder_1.jpg`
- `cover_placeholder_2.jpg`
- `cover_placeholder_3.jpg`
- `cover_placeholder_4.jpg`
- `cover_placeholder_5.jpg`

任意网图都行，用于 Module 5 之前的占位。**记得文件名规则：全小写 + 下划线**。

新建一个工具函数获取占位图（先用，到 Module 5 替换）：

```kotlin
// data/mock/MockCoverProvider.kt
package com.example.hongguolite.data.mock

import com.example.hongguolite.R

object MockCoverProvider {
    private val placeholders = listOf(
        R.drawable.cover_placeholder_1,
        R.drawable.cover_placeholder_2,
        R.drawable.cover_placeholder_3,
        R.drawable.cover_placeholder_4,
        R.drawable.cover_placeholder_5,
    )

    /** 根据剧目 id 稳定地返回一张占位图（同一个 id 总是返回同一张） */
    fun getPlaceholder(dramaId: String): Int {
        // 用 id 的 hashCode 取模，保证同一 drama 总是同一张图，不会闪
        val index = (dramaId.hashCode() % placeholders.size + placeholders.size) % placeholders.size
        return placeholders[index]
    }
}
```

### 验收

- [ ] `Drama.kt` 创建完成，理解 data class 的作用
- [ ] `MockDramas.kt` 至少 30 条数据，剧名真实（不是"剧目1"这种）
- [ ] 5 张占位图放进 `res/drawable/`，能正确通过 `R.drawable.cover_placeholder_1` 引用

---

## 子任务 2.2：剧场页顶部

### 做什么

实现剧场页顶部三块：搜索框、一级 Tab（横向滚动）、4 个圆形功能入口。

### 知识点 1：可滚动 Tab

`TabRow` vs `ScrollableTabRow`：

| 组件 | 何时用 | 行为 |
|---|---|---|
| `TabRow` | Tab 数量少（2-4 个） | Tab 平均分屏幕宽度，不可滚动 |
| `ScrollableTabRow` | Tab 数量多（5+） | Tab 按内容宽度排列，可横向滚动 |

剧场页有 7 个 Tab（找剧/社区/漫剧/电影/电视剧/听书/小说），明确用 `ScrollableTabRow`。

### 知识点 2：状态提升（Module 1 已学）

Tab 选中状态由谁管？

- **错误做法**：在 `TheaterTopBar` 内部用 `remember { mutableStateOf(0) }` —— 这样剧场页主体（卡片列表）拿不到当前选中的 Tab，不知道展示什么数据
- **正确做法**：`selectedTabIndex` 在 `TheaterScreen` 顶层管理，`TheaterTopBar` 接收它当参数

### 步骤 1：定义剧场页一级 Tab

新建 `data/model/TheaterTab.kt`：

```kotlin
package com.example.hongguolite.data.model

// 跟 BottomTab 同样的"配置数据"思路
data class TheaterTab(val id: String, val label: String)

val theaterTabs = listOf(
    TheaterTab("find_drama", "找剧"),
    TheaterTab("community", "社区"),
    TheaterTab("manga", "漫剧"),
    TheaterTab("movie", "电影"),
    TheaterTab("tv", "电视剧"),
    TheaterTab("audiobook", "听书"),
    TheaterTab("novel", "小说"),
)
```

### 步骤 2：实现 TheaterTopBar

新建 `ui/screens/theater/TheaterTopBar.kt`：

> 注意子目录：`ui/screens/theater/`，剧场页的所有相关组件都放这里。
> 这是项目结构惯例：**屏幕级文件夹**（feature folder），把一个屏幕用到的所有 Composable 聚在一起。

```kotlin
package com.example.hongguolite.ui.screens.theater

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hongguolite.data.model.theaterTabs

/**
 * 剧场页顶部区域：搜索框 + 一级 Tab + 4 个圆形入口
 *
 * @param selectedTabIndex 当前选中的一级 Tab 下标（由父组件管理）
 * @param onTabSelected 用户切换 Tab 时回调
 * @param onSearchBoxClick 用户点击搜索框时回调（跳转搜索前置页）
 * @param onRankClick 用户点击"排行榜"圆形入口时回调（跳转榜单页）
 */
@Composable
fun TheaterTopBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onSearchBoxClick: () -> Unit,
    onRankClick: () -> Unit,
) {
    Column {
        // ━━━ 第 1 部分：搜索框 ━━━
        SearchBoxStub(onClick = onSearchBoxClick)

        // ━━━ 第 2 部分：一级 Tab ━━━
        // TODO 1：用 ScrollableTabRow 实现 7 个 Tab
        // 提示：
        // - selectedTabIndex 已经是参数
        // - edgePadding = 0.dp 让 Tab 紧贴左边
        // - 每个 Tab 用 Tab(selected=..., onClick=..., text=...)
        // - 文字加粗 vs 普通，可以根据 selectedTabIndex 判断

        // ━━━ 第 3 部分：4 个圆形入口 ━━━
        FunctionEntryRow(onRankClick = onRankClick)
    }
}

/**
 * 顶部的"伪搜索框"——它不能输入，只是个跳转入口
 *
 * 思考：为什么不直接用 TextField？
 * 答：TextField 会获取焦点弹出键盘，但我们的需求是点击即跳转到独立的搜索页。
 * 所以这是个"看起来像搜索框、实际是按钮"的组件，常见模式。
 */
@Composable
private fun SearchBoxStub(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF5F5F5))
            .clickable(onClick = onClick)  // 点击整个 Row 都算点击搜索框
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "搜索",
            tint = Color.Gray,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "我在精神病院学斩神",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)  // 占满中间空间
        )

        // TODO 2：右侧的"📷 截图识别短剧"
        // 提示：
        // - 用 Divider（垂直分隔线）+ Icon + Text
        // - 这部分点击应该有自己的回调（可以再加一个参数 onScreenshotClick），
        //   或者临时全部 Toast"未实现"
    }
}

/**
 * 4 个圆形功能入口
 */
@Composable
private fun FunctionEntryRow(onRankClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        FunctionEntry(
            icon = Icons.Default.FilterList,
            label = "筛选",
            iconBgColor = Color(0xFF8E8FFF),
            onClick = { /* TODO Toast */ }
        )
        FunctionEntry(
            icon = Icons.Default.Whatshot,
            label = "排行榜",
            iconBgColor = Color(0xFFFF6B35),
            onClick = onRankClick  // 这个会真的跳转
        )
        FunctionEntry(
            icon = Icons.Default.PlayCircleOutline,
            label = "新剧",
            iconBgColor = Color(0xFF34C759),
            onClick = { /* TODO Toast */ }
        )
        FunctionEntry(
            icon = Icons.Default.GridView,
            label = "预约",
            iconBgColor = Color(0xFFFF9500),
            onClick = { /* TODO Toast */ }
        )
    }
}

@Composable
private fun FunctionEntry(
    icon: ImageVector,
    label: String,
    iconBgColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = Color.White)
        }
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 13.sp)
    }
}
```

### 步骤 3：实现 TheaterScreen 主屏

新建 `ui/screens/theater/TheaterScreen.kt`：

```kotlin
package com.example.hongguolite.ui.screens.theater

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TheaterScreen(
    onSearchBoxClick: () -> Unit,
    onRankClick: () -> Unit,
) {
    // 顶层管理一级 Tab 的选中状态
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TheaterTopBar(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            onSearchBoxClick = onSearchBoxClick,
            onRankClick = onRankClick,
        )

        // 内容区：根据 selectedTabIndex 切换
        when (selectedTabIndex) {
            0 -> {
                // 找剧 Tab：暂时占位，2.4 实现真实内容
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("找剧 Tab 内容（2.4 实现）")
                }
            }
            else -> {
                // 其他 Tab：占位
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("该分类未实现")
                }
            }
        }
    }
}
```

### 步骤 4：接到 MainScreen 的 NavHost

修改 `MainScreen.kt`，把剧场路由替换：

```kotlin
composable(Routes.THEATER) {
    TheaterScreen(
        onSearchBoxClick = {
            // Module 3 改成 navController.navigate(Routes.SEARCH)
        },
        onRankClick = {
            // Module 4 改成 navController.navigate(Routes.RANK)
        }
    )
}
```

### 验收

- [ ] 剧场 Tab 顶部能看到搜索框（圆角灰底 + 占位文字）
- [ ] 7 个一级 Tab 能横向滚动
- [ ] 选中的 Tab 文字加粗或有颜色变化
- [ ] 切换 Tab 时下方内容变化（找剧 vs 其他）
- [ ] 4 个圆形入口显示正确，颜色不同

### 强制 check-in 1

完成后向我提交：

1. **真机截图**：剧场页完整截图
2. **代码**：`TheaterTopBar.kt` 完整代码（贴文字，不要截图）
3. **回答**：你 TODO 1 的 `ScrollableTabRow` 是怎么实现的？特别是"选中态加粗"的代码

---

## 子任务 2.3：DramaCard 组件设计 [强制 check-in]

### 做什么

设计一个**可复用的剧目卡片**，将来要在剧场、搜索结果页都用。

### 这个任务的真正意义

子任务 2.3 不只是写一个 UI 组件，是练习**接口设计能力**。

新人写组件常犯两个错误：

- **过度耦合**：直接接收 `Drama` 对象，把所有展示逻辑塞进卡片内部。结果：搜索结果页要"剧名关键词高亮"，发现 DramaCard 改不动
- **过度抽象**：参数列表 20 个，每个都是 `String?`，结果调用方一头雾水

我的目标是让你设计一个**接口干净、扩展灵活**的组件。

### 步骤 1：先思考接口（在动手写代码前）

先问自己 4 个问题，**不写代码，先在心里或纸上回答**：

1. DramaCard 应该接收 `Drama` 对象，还是接收一堆零散字段（title、coverUrl 等）？为什么？
2. 卡片被点击了，DramaCard 自己处理跳转，还是回调给父组件？
3. 搜索结果页要"关键词高亮剧名"——这个需求怎么在 DramaCard 接口里预留？
4. Module 5 要把 `coverUrl` 从本地资源换成网络图——会改 DramaCard 接口吗？

**这 4 个问题不答清楚，下面的代码不要写。** 想 5 分钟，给我贴答案。

### 步骤 2：实现 DramaCard

得到我对你接口设计的反馈后，再实现。这一步我先不给脚手架，等看了你的设计思路再决定。

---

## 子任务 2.4：找剧 Tab 双列卡片瀑布流

### 做什么

把找剧 Tab 下的占位文字替换成双列卡片瀑布流，至少展示 30 条 Mock 数据。

### 知识点

`LazyVerticalGrid` 是 Compose 的"网格列表"，对应原生 `RecyclerView + GridLayoutManager`。

- `GridCells.Fixed(2)`：固定 2 列
- `GridCells.Adaptive(120.dp)`：每个 cell 最小 120dp，根据屏幕宽度自动决定列数

### 提示（不给完整脚手架，自己写）

```kotlin
@Composable
fun FindDramaContent(dramas: List<Drama>, onDramaClick: (Drama) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = dramas,
            key = { it.id }  // 关键：用 id 作为 key，提升性能 + 滚动状态保留
        ) { drama ->
            DramaCard(
                drama = drama,
                onClick = { onDramaClick(drama) }
            )
        }
    }
}
```

### 新手坑预警

1. **不写 `key`**：滚动到中间删除一项，列表全部刷新（视觉上闪一下）
2. **嵌套 LazyColumn / LazyVerticalGrid**：编译报错 "Vertically scrollable component was measured with an infinite max constraints"
3. **GridCells.Fixed(2) 但 padding 占满**：卡片宽度算错，可能溢出屏幕

### 验收

- [ ] 切到剧场→找剧，看到双列卡片
- [ ] 至少 30 张卡片，可以流畅滚动
- [ ] 点击卡片有反馈（Toast 或日志即可，详情页不实现）
- [ ] 角标【爆剧】【新剧】数据驱动显示，不是每张都有

---

## 子任务 2.5：联调 + 强制 check-in 2

### 做什么

1. 完整跑一遍：底部 Tab "剧场" → 看到顶部+卡片 → 点搜索框 → Toast "搜索页未实现" → 点排行榜 → Toast "榜单未实现"
2. Git commit

### Commit 节奏建议

```bash
git commit -m "feat(module2): 添加 Drama 数据模型和 Mock 数据"
git commit -m "feat(module2): 实现剧场页顶部（搜索框+Tab+功能入口）"
git commit -m "feat(module2): 实现可复用 DramaCard 组件"
git commit -m "feat(module2): 找剧 Tab 双列瀑布流"
```

### 强制 check-in 2（Module 2 完成）

完成后提供：

1. **真机视频或多张截图**：展示剧场页滚动 + Tab 切换
2. **当场改需求**：我会给你一个改动需求，你需要**当场实现并发改动后的截图**

---

## Module 2 完成后你应该掌握的

- [ ] data class、sealed class 的使用场景
- [ ] `LazyVerticalGrid` 的 API 和性能要点（`key` 参数）
- [ ] `ScrollableTabRow` vs `TabRow` 的选择
- [ ] **可复用组件的接口设计原则**（这是 2.3 的核心）
- [ ] feature 文件夹的组织方式（`screens/theater/` 的子目录结构）

---

**接下来**：从子任务 2.1 开始。完成 2.1 后**不需要 check-in**，直接做 2.2。
2.2 完成后**强制 check-in 1**。

去做吧。
