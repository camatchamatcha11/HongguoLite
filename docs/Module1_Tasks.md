# Module 1：项目骨架 + 底部 5 Tab + 占位页 + 首页静态版

> **预估时间**：1 - 1.5 天 **核心目标**：让 Demo 一打开就像个 App **学习重点**：Compose 项目结构、Navigation Compose、Modifier 链式调用、状态栏处理

------

## 子任务总览

| 子任务 | 内容                                   | 预估时间  | Check-in |
| ------ | -------------------------------------- | --------- | -------- |
| 1.1    | 创建项目 + 跑通 Hello World            | 30 min    | ✅ 强制   |
| 1.2    | 添加 Navigation 依赖 + 5 个空 Tab 切换 | 1.5 - 2 h | ✅ 强制   |
| 1.3    | Tier 3 占位页（商城/赚钱/我的）        | 30 min    | ❌        |
| 1.4    | 首页静态版（背景图 + 顶部图标）        | 1 - 1.5 h | ❌        |
| 1.5    | 首页右侧悬浮按钮列                     | 1 - 1.5 h | ❌        |
| 1.6    | 首页底部信息区                         | 1.5 - 2 h | ❌        |
| 1.7    | 联调 + Git 提交 + 完整演示             | 30 min    | ✅ 强制   |

------

## 子任务 1.1：创建项目 + 跑通 Hello World

### 做什么

用 Android Studio 新建一个 Empty Activity 项目，配置好基本参数，确保能在你的 vivo 上跑起来。

### 操作步骤

1. Android Studio → File → New → New Project
2. 选择模板：**Empty Activity**（注意：不是 Empty Views Activity！）
3. 填写：
   - Name: `HongguoLite`
   - Package name: `com.example.hongguolite`
   - Save location: 你自己选个干净的目录
   - Language: **Kotlin**
   - Minimum SDK: **API 24 (Android 7.0)**
   - Build configuration language: **Kotlin DSL (build.gradle.kts) [Recommended]**
4. 等 Gradle Sync 完成（第一次比较慢，10-20 分钟正常）
5. 真机连上 USB，顶部 Run

### 验收

- 你的 vivo 上能看到一个白底页面，中间显示 "Hello Android!"

### 完成后向我报告

在对话里发：

> 子任务 1.1 完成。

我会让你截图项目结构（左侧 Project 面板的截图）。

------

## 子任务 1.2：底部 5 Tab 导航

### 做什么

实现 5 个底部 Tab：首页、剧场、商城、赚钱、我的。点击能切换。每个 Tab 暂时只显示一个 `Text` 占位。

### 知识点（你需要先理解）

1. **Navigation Compose**：Google 官方推荐的页面路由方案
2. **NavController**：管理页面跳转的"控制器"
3. **NavHost**：定义"哪个路由对应哪个页面"的容器
4. **NavigationBar / NavigationBarItem**：Material3 的底部导航组件

### 必须查阅的官方文档

⚠️ **不要直接照抄我下面的代码！先去看官方文档，理解后再回来动手。**

- [Compose 中的导航](https://developer.android.com/develop/ui/compose/navigation?hl=zh-cn)
- [底部导航栏 NavigationBar](https://developer.android.com/develop/ui/compose/components/navigation-bar?hl=zh-cn)

### 步骤 1：添加依赖

打开 `app/build.gradle.kts`，在 `dependencies { ... }` 块里加：

```kotlin
// Navigation Compose（自己去查最新版本号，不要硬编码我给的版本）
implementation("androidx.navigation:navigation-compose:2.7.7")
```

加完后点 Android Studio 顶部弹出的 **Sync Now**。

### 步骤 2：定义路由常量

新建一个 Kotlin 文件 `Routes.kt`，路径 `com.example.hongguolite/navigation/Routes.kt`：

```kotlin
package com.example.hongguolite.navigation

// 用 object 定义路由常量。新人坑：不要用魔法字符串散落在代码各处。
object Routes {
    const val HOME = "home"
    const val THEATER = "theater"
    const val SHOP = "shop"
    const val EARN = "earn"
    const val PROFILE = "profile"
}
```

### 步骤 3：定义 Tab 数据模型

新建 `BottomTab.kt`，路径 `com.example.hongguolite/ui/components/BottomTab.kt`：

```kotlin
package com.example.hongguolite.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.hongguolite.navigation.Routes

// data class：自动帮你生成 equals/hashCode/toString，比 Java 的 POJO 简洁多了
data class BottomTabItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

// 注意：这是 PRD 里说的"Tab 列表配置化"——加 Tab 只改这个列表，不改 UI 代码
val bottomTabs = listOf(
    BottomTabItem(Routes.HOME, "首页", Icons.Default.Home),
    BottomTabItem(Routes.THEATER, "剧场", Icons.Default.Star),
    BottomTabItem(Routes.SHOP, "商城", Icons.Default.ShoppingCart),
    BottomTabItem(Routes.EARN, "赚钱", Icons.Default.AttachMoney),
    BottomTabItem(Routes.PROFILE, "我的", Icons.Default.Person),
)
```

⚠️ **新手坑**：导入 `Icons` 时要导对——是 `androidx.compose.material.icons.Icons`。如果用 `material-icons-extended` 库，会有更多图标可选，但 Module 1 用基础图标就够。

### 步骤 4：实现 MainScreen（脚手架，需要你填空）

新建 `MainScreen.kt`，路径 `com.example.hongguolite/ui/MainScreen.kt`：

```kotlin
package com.example.hongguolite.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hongguolite.navigation.Routes
import com.example.hongguolite.ui.components.bottomTabs

@Composable
fun MainScreen() {
    // 创建 NavController：rememberNavController 保证它在重组中保留
    val navController = rememberNavController()

    // 监听当前路由，用于决定 Tab 的选中态
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomTabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            // TODO 1：实现路由跳转
                            // 提示：用 navController.navigate(tab.route) { ... }
                            // 提示：要避免重复 push 同一个路由（用 launchSingleTop = true）
                            // 提示：切换 Tab 时要清掉之前的栈（用 popUpTo + saveState/restoreState）
                            // 去查官方文档"Bottom navigation"那一节
                        },
                        icon = {
                            // TODO 2：显示 tab.icon
                        },
                        label = {
                            // TODO 3：显示 tab.label
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,  // 默认起始页：首页
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)  // 重要：避免内容被底部 Tab 遮挡
        ) {
            // TODO 4：定义每个路由对应的页面
            // 提示：composable(Routes.HOME) { ... }
            // 每个页面暂时只放一个 Text("首页页面") 之类的占位
            // 后续 Module 会把这些页面替换为真实内容
        }
    }
}
```

### 步骤 5：把 MainScreen 接到 MainActivity

修改 `MainActivity.kt`，把 `setContent` 里的内容换成 `MainScreen()`：

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
        HongguoLiteTheme {
            MainScreen()  // 把整个 App 入口换成 MainScreen
        }
    }
}
```

### 步骤 6：你需要填的 4 个 TODO

仔细看上面代码里的 4 个 TODO 注释，你需要查文档自己实现。

特别是 TODO 1 的路由跳转，**这是 Compose Navigation 最容易踩坑的地方**：

新手坑预警：

- 直接 `navController.navigate(route)` 会重复 push 同一个路由，按返回键时要按 5 次才能退出 App
- 切换 Tab 时不处理栈状态，会导致返回键行为奇怪
- 这两个问题官方文档里有标准答案，去查"Navigate to a destination from a bottom navigation bar"

### 验收

- [ ] 5 个 Tab 显示正确，能看到图标和文字
- [ ] 选中的 Tab 高亮，未选中的灰色
- [ ] 点击 Tab 能切换页面，每个页面显示对应的占位文字
- [ ] **按手机返回键，App 直接退出**（不会在 Tab 之间反复回退）

### 完成后向我报告

```
子任务 1.2 完成。
```

我会让你：

1. 截图 5 Tab 页面
2. 把你的 `MainScreen.kt` 完整代码贴出来
3. 回答两个问题：
   - `launchSingleTop = true` 是什么作用？
   - `popUpTo + saveState + restoreState` 三件套解决了什么问题？

------

## 子任务 1.3：Tier 3 占位页

### 做什么

为商城、赚钱、我的三个 Tab 创建占位页面，统一样式。

### 脚手架（这个简单，给完整代码）

新建 `PlaceholderScreen.kt`，路径 `com.example.hongguolite/ui/screens/PlaceholderScreen.kt`：

```kotlin
package com.example.hongguolite.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun PlaceholderScreen(
    icon: ImageVector,
    title: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(80.dp),
            tint = Color.Gray
        )
        Text(
            text = "$title 模块为 Demo 占位，未实现",
            color = Color.Gray
        )
    }
}
```

### 你要做的

回到 `MainScreen.kt`，把 `NavHost` 里商城/赚钱/我的三个路由对应到这个占位页：

```kotlin
composable(Routes.SHOP) {
    PlaceholderScreen(icon = Icons.Default.ShoppingCart, title = "商城")
}
// ...类似处理赚钱和我的
```

### 验收

- [ ] 切到商城/赚钱/我的，看到对应图标和文字
- [ ] 三个页面样式一致

------

## 子任务 1.4：首页静态版 - 背景图 + 顶部图标

### 做什么

用一张静态背景图覆盖整个屏幕，叠加左上角的汉堡菜单 + 右上角的搜索图标。

### 准备资源

1. 把你之前发给导师的"首页.jpg"放到 `app/src/main/res/drawable/` 目录下，重命名为 `home_bg.jpg`
2. 注意：drawable 目录下的图片**文件名必须全小写 + 下划线**，不能有大写字母或连字符（这是 Android 的硬性规则）

### 知识点

- `Image` 组件 + `painterResource` 加载本地图片
- `Box` 实现层叠布局（背景在底层，元素叠在上面）
- `Modifier.fillMaxSize()` 充满整个父容器
- `IconButton` 可点击的图标按钮

### 脚手架（需要你填空）

新建 `HomeScreen.kt`，路径 `com.example.hongguolite/ui/screens/HomeScreen.kt`：

```kotlin
package com.example.hongguolite.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.widget.Toast  // 错的！这只是示意，正确导入需要你查
import com.example.hongguolite.R

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
            contentScale = ContentScale.Crop  // 等比缩放并裁剪，避免变形
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
                // TODO：弹 Toast 提示"侧边栏未实现"
                // 提示：用 android.widget.Toast.makeText(context, "...", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "菜单",
                    tint = Color.White
                )
            }

            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = Color.White
                )
            }
        }

        // 第 3 层：右侧悬浮按钮列（子任务 1.5 实现）
        // TODO 1.5

        // 第 4 层：底部信息区（子任务 1.6 实现）
        // TODO 1.6
    }
}
```

### 把 HomeScreen 接到 NavHost

回到 `MainScreen.kt`，把首页路由替换成 `HomeScreen`：

```kotlin
composable(Routes.HOME) {
    HomeScreen(
        onSearchClick = {
            // 暂时弹个 Toast，搜索页 Module 3 才实现
            // 等 Module 3 改成 navController.navigate(Routes.SEARCH)
        }
    )
}
```

### 新手坑预警

1. **图片文件名命名错误** → drawable 文件名只允许小写字母、数字、下划线
2. **R.drawable.home_bg 不存在** → 要确保图片真的放在 `res/drawable/` 下，并且 Build → Make Project 让 R 类重新生成
3. **背景图把状态栏挡住或留白** → 这是正常的，需要用 `enableEdgeToEdge()`（你 MainActivity 里默认有）+ 设置状态栏图标颜色，下个 Module 处理
4. **Toast 导入错误** → 应该是 `android.widget.Toast`

### 验收

- [ ] 切到首页 Tab，能看到全屏背景图
- [ ] 左上角汉堡菜单，右上角搜索图标，都是白色
- [ ] 点击汉堡，弹 Toast "侧边栏未实现"
- [ ] 点击搜索，弹 Toast 或暂时不报错（搜索页待实现）

------

## 子任务 1.5：首页右侧悬浮按钮列

### 做什么

在 `HomeScreen` 的 `Box` 里加一列右侧悬浮按钮：收藏、评论、点赞、分享。每个按钮 = 图标 + 数字。

### 提示（这次给少一点，让你练习自己实现）

- 用一个 `Column` 包裹所有按钮
- `Column` 用 `.align(Alignment.CenterEnd)` 贴右侧居中
- 每个按钮 = `Column` { Icon, Text }
- 数字数据可以写死（参考截图：40.2万、2221、14.3万、1万）
- 颜色：图标白色、数字白色
- 间距：每个按钮之间留 16dp 左右

### 涉及的图标（Material Icons）

- 收藏：`Icons.Default.Star`
- 评论：`Icons.Default.ChatBubbleOutline`（需要 material-icons-extended，或用 `Icons.Default.Email` 替代）
- 点赞：`Icons.Default.Favorite`
- 分享：`Icons.Default.Share`

### 安装 material-icons-extended（可选）

如果你想用更丰富的图标，添加依赖：

```kotlin
implementation("androidx.compose.material:material-icons-extended:1.6.0")
```

### 验收

- [ ] 4 个按钮垂直排列在屏幕右侧中间偏下位置
- [ ] 每个按钮有图标 + 数字
- [ ] 整体不挡住中间的关键内容

------

## 子任务 1.6：首页底部信息区

### 做什么

在 `HomeScreen` 的 `Box` 里加底部信息区，参考截图最下面的部分（爆剧标签 + 剧名 + 类型胶囊 + 简介 + "观看完整短剧"卡片）。

### 提示

- 用一个 `Column` 包裹所有元素
- `Column` 用 `.align(Alignment.BottomCenter)` 贴底
- 注意要留出底部 Tab 的高度（已经被 `Scaffold` 的 innerPadding 处理了，不用你管）

### 元素拆解

1. **【爆剧】红色小标签**：用 `Box` + 红色背景 + 圆角 + 内部 Text
2. **剧名**："全球冰封：开局手握百…"，白色加粗，超过宽度省略
3. **类型胶囊行**：用 `Row` 包两个胶囊（科幻末世、都市脑洞），灰底圆角
4. **简介行**："第1集 | 极寒末日降临，全球九…"，白色，单行省略
5. **"观看完整短剧"卡片**：圆角矩形，深色半透明背景，左侧播放图标 + 文字"观看完整短剧 · 全75集"

### 关键技术点

- **圆角胶囊**：`Modifier.clip(RoundedCornerShape(...))` 然后 `.background(...)`
- **文字省略**：`Text(maxLines = 1, overflow = TextOverflow.Ellipsis)`
- **半透明背景**：`Color.Black.copy(alpha = 0.5f)`

### 不需要做

- 不需要进度条
- 不需要"作者声明：内容由 AI 生成"
- 不需要"展开"按钮的展开动画

### 验收

- [ ] 底部能看到爆剧标签、剧名、类型胶囊、简介、观看卡片
- [ ] 整体看起来跟红果首页"差不多"（不要求像素级还原）
- [ ] 文字超长正确省略，不会溢出屏幕

------

## 子任务 1.7：联调 + Git 提交 + 完整演示

### Git 提交节奏

如果你之前没分阶段 commit，现在补上。建议 commit 节奏：

```bash
git add .
git commit -m "feat: 创建项目骨架"

# 完成 1.2 后
git commit -m "feat: 实现底部 5 Tab 导航"

# 完成 1.3 后
git commit -m "feat: 添加 Tier 3 占位页"

# 完成 1.4-1.6 后
git commit -m "feat: 完成首页静态版（背景+悬浮按钮+底部信息区）"
```

### 完整演示

打开 App，跑一遍完整流程：

1. 启动 → 看到首页静态版
2. 点击底部"剧场" → 看到剧场占位文字
3. 点击底部"商城" → 看到商城占位
4. 点击底部"赚钱" → 看到赚钱占位
5. 点击底部"我的" → 看到我的占位
6. 回到"首页" → 测试右上角搜索图标点击
7. 按手机返回键 → App 直接退出

### 完成后向我报告

```
Module 1 全部完成。
```

我会让你：

1. 录一个 30 秒视频或多张截图展示完整流程
2. 提一个改动需求当场实现（比如"把底部 Tab 选中色改成红色"）
3. 解释你 `MainScreen.kt` 里某段代码

------

## Module 1 完成后你应该掌握的

- [ ] Compose 项目的基本结构（res、kotlin、Manifest 各自的作用）
- [ ] Navigation Compose 的核心 API（NavController、NavHost、composable）
- [ ] 底部 Tab 切换的标准实现模式
- [ ] `Box`、`Column`、`Row` 三大基础布局
- [ ] `Modifier` 的链式调用
- [ ] 本地图片资源的加载
- [ ] data class 的使用
- [ ] 学到了至少 1 个新的"新手坑"

如果上面任何一项你不能用一两句话讲清楚，回去翻代码，确保你真的理解了，而不是抄通了。
