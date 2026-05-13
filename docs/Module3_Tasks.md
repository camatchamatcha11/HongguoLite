# Module 3：搜索模块（三态搜索：前置页 + 中态联想 + 结果页）

> **预估时间**：2.5 - 3 天
> **PRD 定位**：整个 Demo 最核心、最能体现工程能力的部分
> **核心目标**：搭建可扩展的搜索架构，掌握 Compose 异步、状态机、持久化
> **难度跃升**：第一次涉及协程 / 防抖 / Repository 抽象 / DataStore 持久化

---

## ⚠️ 写在最前面：Module 3 不一样

前两个 Module 是 **UI 工程师的活**——用 Compose 摆 UI、状态提升、组件复用。
Module 3 是 **应用工程师的活**——你要建一个**有数据流、有异步、有持久化、有状态机**的小型架构。

---

## 子任务总览

| 子任务 | 内容 | 强制 check-in（与我） |
|---|---|---|
| 3.1 | 架构理解 + Route 接入（在 NavHost 里加搜索路由） | ❌ |
| 3.2 | Repository 抽象 + Mock 数据扩充 | ✅ 必须发 SearchRepository 接口设计 + 自评 |
| 3.3 | ViewModel + StateFlow + 防抖（搜索引擎核心） | ✅ 必须发 ViewModel + Flow 链 + 自评 |
| 3.4 | 搜索前置页 UI（猜你想搜 + 热搜榜 + 历史） | ❌ |
| 3.5 | 搜索中态 UI + 关键词高亮（AnnotatedString） | ❌ |
| 3.6 | 搜索结果页 + 分类 Tab + DramaCard 复用 | ❌ |
| 3.7 | DataStore 持久化搜索历史 | ❌ |
| 3.8 | Module 3 整体汇总同步 | ❌ |

---

## 1. 架构设计（动手前必读）

### 1.1 你将要建的"小架构"长什么样

```
┌─────────────────────────────────────────────┐
│  UI Layer (Compose)                         │
│  - SearchScreen                             │
│  - SearchPreContent (前置页)                │
│  - SearchSuggestContent (中态)              │
│  - SearchResultContent (结果页)             │
└──────────────┬──────────────────────────────┘
               │ collect StateFlow
               ▼
┌─────────────────────────────────────────────┐
│  SearchViewModel                            │
│  - StateFlow<SearchUiState>                 │
│  - 处理用户输入（防抖）                       │
│  - 调用 Repository                          │
└──────────────┬──────────────────────────────┘
               │ suspend fun
               ▼
┌─────────────────────────────────────────────┐
│  SearchRepository (interface)               │
│  - searchDramas(keyword): List<Drama>       │
│  - getHotSearches(): List<Drama>            │
│  - getGuessLikes(): List<Drama>             │
│  - getSearchHistory(): Flow<List<String>>   │
│  - addSearchHistory(keyword)                │
│  - clearSearchHistory()                     │
└──────────────┬──────────────────────────────┘
               │
       ┌───────┴───────┐
       ▼               ▼
┌─────────────┐  ┌─────────────────┐
│ MockSearch  │  │ DataStore       │
│ Repository  │  │ (历史记录持久化) │
│ (内存+Mock) │  │                 │
└─────────────┘  └─────────────────┘
```

**关键设计原则**（接你 Module 2 学到的）：

- **UI 不知道数据从哪来**（只 collect StateFlow）
- **ViewModel 不知道存储细节**（只调 Repository 接口）
- **Repository 是接口**（Module 5 换网络实现时，UI 和 ViewModel 一个字不改）
- **状态由 ViewModel 集中管理**（不是各种 remember 散落 UI 里）

### 1.2 三态状态机

```
                    ┌─────────────┐
       (打开搜索页) │  PreSearch   │ ← 显示猜你想搜 + 热搜榜 + 历史
                    └──────┬──────┘
                           │ 输入文字
                           ▼
                    ┌─────────────┐
                    │ Suggesting  │ ← 显示联想列表
                    └──────┬──────┘
                           │ 点搜索 / 点联想项
                           ▼
                    ┌─────────────┐
                    │  Showing-   │ ← 显示搜索结果
                    │   Result    │
                    └─────────────┘

任意状态 → 清空搜索框 → 回到 PreSearch
任意状态 → 点返回 → 关闭搜索页
```

这就是为什么 PRD 强调"搜索三态"——状态机让代码清晰、Bug 少。

### 1.3 关键技术点速览（每个后面详讲）

| 概念 | 一句话 | 对应 Java 概念 |
|---|---|---|
| `ViewModel` | 状态容器，存活时间比 Composable 长 | Servlet 的 Session 作用域对象 |
| `StateFlow<T>` | 类似 RxJava 的 BehaviorSubject，有当前值的"流" | 你应该懂 Observable 模式 |
| `Flow.debounce(300ms)` | 上游 300ms 内没新值才往下发 | RxJava 的 debounce 操作符 |
| `viewModelScope.launch` | 在 ViewModel 生命周期内启动协程 | 类似 ExecutorService.submit 但更轻量 |
| `AnnotatedString` | 富文本字符串（带样式段落） | Java 里的 AttributedString |
| `DataStore<Preferences>` | Android 推荐的轻量 KV 持久化 | 类似 properties 文件，但是异步的 |

---

## 2. 子任务详解

### 子任务 3.1：架构理解 + 路由接入

#### 做什么

1. 把上面 §1 完整读一遍，理解架构图和状态机
2. 在 `Routes.kt` 加入搜索路由
3. 在 `MainScreen.kt` 的 `NavHost` 里注册搜索路由（暂时指向占位）
4. 把剧场页的搜索框点击、首页的搜索图标点击对接到搜索页

#### 步骤 1：路由定义

`Routes.kt` 加一行：

```kotlin
object Routes {
    const val HOME = "home"
    const val THEATER = "theater"
    const val SHOP = "shop"
    const val EARN = "earn"
    const val PROFILE = "profile"
    const val SEARCH = "search"  // ← 新增
}
```

#### 步骤 2：在 NavHost 里注册

`MainScreen.kt` 的 `NavHost` 里加：

```kotlin
composable(Routes.SEARCH) {
    // 暂时占位，3.4 实现真实内容
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("搜索页占位")
    }
}
```

#### 步骤 3：剧场和首页的入口对接

```kotlin
// MainScreen.kt 的 Routes.THEATER composable
composable(Routes.THEATER) {
    TheaterScreen(
        onSearchBoxClick = {
            navController.navigate(Routes.SEARCH)  // ← 真跳转
        },
        ...
    )
}

// MainScreen.kt 的 Routes.HOME composable
composable(Routes.HOME) {
    HomeScreen(
        onSearchClick = {
            navController.navigate(Routes.SEARCH)  // ← 真跳转
        }
    )
}
```

#### 思考题（不用答）

为什么搜索页**不是底部 Tab**，而是一个独立路由？跟 Tab 的体验差异是什么？

提示：注意搜索页有"返回箭头"，Tab 没有。

#### 验收

- [ ] 真机：从剧场顶部搜索框点击 → 跳到"搜索页占位"
- [ ] 真机：从首页右上搜索图标点击 → 跳到"搜索页占位"
- [ ] 真机：在搜索页按系统返回键 → 回到上一页（剧场或首页）

---

### 子任务 3.2：Repository 抽象

#### 做什么

设计并实现 `SearchRepository` 接口 + Mock 实现 + 扩充 Mock 数据。

这是**整个 Module 3 的地基**。地基设计得好，后面的 ViewModel 和 UI 都会顺。

#### 步骤 1：先思考接口（动手前必读）

像 Module 2 的 DramaCard 4 问一样，先思考再写代码。**我会在 check-in 时验你的思考过程**。

**4 个问题**：

1. **接口里哪些方法返回 `List<T>`，哪些返回 `Flow<List<T>>`？凭什么标准区分？**
   - 提示：搜索历史是会变化的（你删一条它就变），猜你想搜你刷新才变。这两个谁该是 Flow？

2. **`searchDramas(keyword: String)` 应该是 `suspend fun` 还是 `fun`？为什么？**
   - 提示：当前 Mock 实现是同步的，但 Module 5 接网络时是异步的。如果现在不写 suspend，Module 5 改不动。

3. **写入操作（addSearchHistory）需要返回值吗？**
   - 提示：DataStore 写入会失败吗？UI 需要知道写入结果吗？

4. **接口要不要返回错误信息？**
   - 提示：搜索失败时（网络错误）怎么告诉 UI？是接口返回 `Result<T>`，还是抛异常？

**回答这 4 个问题，给我后再写代码。**

#### 步骤 2：接口定义（脚手架，等你 4 问通过后实现）

```kotlin
// data/repository/SearchRepository.kt
package com.example.hongguolite.data.repository

import com.example.hongguolite.data.model.Drama
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    /** 按关键词搜索剧目（模糊匹配剧名） */
    suspend fun searchDramas(keyword: String): List<Drama>

    /** 联想（带封面） */
    suspend fun suggestDramas(keyword: String): List<Drama>

    /** 联想（纯文字） */
    suspend fun suggestKeywords(keyword: String): List<String>

    /** 猜你想搜（一组推荐剧目，刷新会变） */
    suspend fun getGuessLikes(): List<Drama>

    /** 热搜榜（前 N 名剧目） */
    suspend fun getHotSearches(): List<Drama>

    /** 搜索历史（持久化、会变化） */
    fun getSearchHistory(): Flow<List<String>>

    /** 添加一条搜索历史（重复会被提到最前，超过 10 条会丢弃最旧的） */
    suspend fun addSearchHistory(keyword: String)

    /** 删除一条 */
    suspend fun removeSearchHistory(keyword: String)

    /** 清空全部 */
    suspend fun clearSearchHistory()
}
```

#### 步骤 3：Mock 实现（搜索历史先用内存版，3.7 换 DataStore）

```kotlin
// data/repository/MockSearchRepository.kt
package com.example.hongguolite.data.repository

import com.example.hongguolite.data.mock.MockDramas
import com.example.hongguolite.data.model.Drama
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockSearchRepository : SearchRepository {

    // 内存搜索历史。3.7 会替换为 DataStore。
    private val _history = MutableStateFlow<List<String>>(emptyList())

    override suspend fun searchDramas(keyword: String): List<Drama> {
        delay(200)  // 模拟网络延迟，让 Loading 状态能被看到
        if (keyword.isBlank()) return emptyList()
        return MockDramas.theaterList.filter {
            it.title.contains(keyword, ignoreCase = true)
        }
    }

    override suspend fun suggestDramas(keyword: String): List<Drama> {
        delay(100)
        if (keyword.isBlank()) return emptyList()
        return MockDramas.theaterList
            .filter { it.title.contains(keyword, ignoreCase = true) }
            .take(3)  // 联想只取前 3 条带封面的
    }

    override suspend fun suggestKeywords(keyword: String): List<String> {
        delay(100)
        if (keyword.isBlank()) return emptyList()
        // TODO 1：你来实现：从 MockDramas 里提取所有含关键词的剧名，
        //          再加几条变体（如 "$keyword+重生"、"$keyword 系列"），
        //          一共返回 5-8 条
        return emptyList()
    }

    override suspend fun getGuessLikes(): List<Drama> {
        delay(150)
        return MockDramas.theaterList.shuffled().take(6)
    }

    override suspend fun getHotSearches(): List<Drama> {
        delay(150)
        return MockDramas.theaterList.take(9)  // 取前 9 条作为榜单
    }

    override fun getSearchHistory(): StateFlow<List<String>> = _history.asStateFlow()

    override suspend fun addSearchHistory(keyword: String) {
        if (keyword.isBlank()) return
        val current = _history.value.toMutableList()
        current.remove(keyword)  // 已存在的话先删
        current.add(0, keyword)  // 新的加到最前
        _history.value = current.take(10)  // 最多保留 10 条
    }

    override suspend fun removeSearchHistory(keyword: String) {
        _history.value = _history.value - keyword
    }

    override suspend fun clearSearchHistory() {
        _history.value = emptyList()
    }
}
```

#### 步骤 4：扩充 Mock 数据

Module 2 你已经有 30 条剧目。Module 3 的联想测试需要"剧名包含同一关键词"的剧目能匹配多条。

**检查你的 Mock 数据**，确保：
- 至少有 3 条剧名含"末世"
- 至少有 3 条剧名含"霸总"或"豪门"
- 至少有 3 条剧名含"重生"

如果不够，**补到 40 条**。

#### 思考题（不用答，但 check-in 我可能问）

`MockSearchRepository` 是单例还是每次 new 一个？为什么？现在你写成 class，可不可以？将来 Module 5 怎么改？

提示：搜索历史在内存里，多个实例会怎样？

#### 验收

- [ ] `SearchRepository.kt` 接口定义完整
- [ ] `MockSearchRepository.kt` 实现完成，TODO 1 你自己实现
- [ ] Mock 数据扩充到 40 条，含多组同关键词剧目
- [ ] 4 个思考题在心里有答案

#### 强制 check-in（与我）

完成后给我：

1. **4 个接口设计问题的回答**（贴文字，逐条答）
2. **`SearchRepository.kt` 完整代码**（贴文字）
3. **你的自评**：
   - 这个子任务训练了你画像里的哪个弱点？
   - 你在 4 问中最纠结的是哪个？为什么？

---

### 子任务 3.3：ViewModel + 防抖（核心引擎）

#### 做什么

实现 `SearchViewModel`，掌握 Compose 异步处理的标准模式。

这是 Module 3 **技术含量最高**的部分。

#### 步骤 1：依赖添加

`app/build.gradle.kts`：

```kotlin
// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
// 协程
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

#### 步骤 2：定义 UI 状态

```kotlin
// ui/screens/search/SearchUiState.kt
package com.example.hongguolite.ui.screens.search

import com.example.hongguolite.data.model.Drama

/**
 * 搜索页的三态。用 sealed interface 表达，编译器会强制你穷举处理所有情况。
 *
 * 注意：这跟"搜索框是否有文字"是两个独立的维度。
 *   - PreSearch：刚进搜索页，没输入任何东西
 *   - Suggesting：正在输入，下方显示联想
 *   - ShowingResult：用户已经点了搜索/选了联想项
 */
sealed interface SearchUiState {
    val keyword: String

    /** 前置态：显示猜你想搜 + 热搜 + 历史 */
    data class PreSearch(
        override val keyword: String = "",
        val guessLikes: List<Drama> = emptyList(),
        val hotSearches: List<Drama> = emptyList(),
        val history: List<String> = emptyList(),
    ) : SearchUiState

    /** 中态：用户在输入，显示联想 */
    data class Suggesting(
        override val keyword: String,
        val dramaSuggestions: List<Drama> = emptyList(),
        val keywordSuggestions: List<String> = emptyList(),
    ) : SearchUiState

    /** 结果态：展示过滤后的剧目 */
    data class ShowingResult(
        override val keyword: String,
        val results: List<Drama> = emptyList(),
        val isLoading: Boolean = false,
    ) : SearchUiState
}
```

#### 步骤 3：ViewModel 主体（脚手架，关键处 TODO 留白）

```kotlin
// ui/screens/search/SearchViewModel.kt
package com.example.hongguolite.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hongguolite.data.repository.SearchRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repo: SearchRepository
) : ViewModel() {

    // 用户输入的关键词（实时，每个字符都触发）
    private val _keywordInput = MutableStateFlow("")

    // 对外暴露的 UI 状态
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.PreSearch())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadPreSearchContent()
        observeKeywordInput()
        observeHistory()
    }

    /** 加载前置页内容（猜你想搜 + 热搜） */
    private fun loadPreSearchContent() {
        viewModelScope.launch {
            val guess = repo.getGuessLikes()
            val hot = repo.getHotSearches()
            // TODO 1：把 guess 和 hot 写进 _uiState
            //         注意：要保留当前的 keyword 和 history
            //         提示：用 _uiState.update { ... } 或 _uiState.value = ...
        }
    }

    /** 观察用户输入 + 防抖 + 触发联想 */
    private fun observeKeywordInput() {
        viewModelScope.launch {
            _keywordInput
                .debounce(300)  // ← 关键：300ms 内没新输入才往下走
                .distinctUntilChanged()  // ← 关键：同样的值不重复触发
                .collect { keyword ->
                    if (keyword.isBlank()) {
                        // 清空 → 回到前置态
                        loadPreSearchContent()
                    } else {
                        // 有输入 → 加载联想
                        loadSuggestions(keyword)
                    }
                }
        }
    }

    /** 观察持久化的搜索历史 */
    private fun observeHistory() {
        viewModelScope.launch {
            repo.getSearchHistory().collect { history ->
                // TODO 2：history 变了，要不要更新 UI？
                //         注意：只有在 PreSearch 状态下，history 才会显示
                //         如果当前在 Suggesting 或 ShowingResult，history 更新不应该影响 UI
                //         提示：用 _uiState.update { state -> ... }
                //               检查 state is PreSearch 才更新
            }
        }
    }

    /** 用户输入了字符（每次按键都调用） */
    fun onKeywordChange(newKeyword: String) {
        _keywordInput.value = newKeyword
        // 立刻切到 Suggesting 状态（但联想数据还没回来，会在 observeKeywordInput 的 collect 里更新）
        _uiState.value = SearchUiState.Suggesting(keyword = newKeyword)
    }

    /** 加载联想数据 */
    private suspend fun loadSuggestions(keyword: String) {
        val dramas = repo.suggestDramas(keyword)
        val keywords = repo.suggestKeywords(keyword)
        // TODO 3：把联想结果写进 _uiState
        //         注意要先检查 _keywordInput.value 是否仍然是这个 keyword
        //         （用户可能已经又输入了新东西，旧的联想结果应该丢弃）
    }

    /** 用户点击"搜索"按钮，或者点了某条联想 */
    fun onSearch(keyword: String) {
        if (keyword.isBlank()) return
        viewModelScope.launch {
            _uiState.value = SearchUiState.ShowingResult(keyword = keyword, isLoading = true)
            repo.addSearchHistory(keyword)
            val results = repo.searchDramas(keyword)
            _uiState.value = SearchUiState.ShowingResult(
                keyword = keyword,
                results = results,
                isLoading = false,
            )
        }
    }

    /** 清空搜索框 → 回到前置态 */
    fun onClearKeyword() {
        _keywordInput.value = ""
        // 状态会通过 observeKeywordInput 切回 PreSearch
    }

    /** 删除一条历史 */
    fun onDeleteHistory(keyword: String) {
        viewModelScope.launch { repo.removeSearchHistory(keyword) }
    }

    /** 清空全部历史 */
    fun onClearAllHistory() {
        viewModelScope.launch { repo.clearSearchHistory() }
    }
}
```

#### 步骤 4：ViewModel 的 Factory（简易依赖注入）

ViewModel 需要传 `SearchRepository`，但 Compose 创建 ViewModel 时不知道怎么传参。需要写个 Factory：

```kotlin
// ui/screens/search/SearchViewModelFactory.kt
package com.example.hongguolite.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hongguolite.data.repository.MockSearchRepository

class SearchViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(MockSearchRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```

**新人坑预警**：每次 `MockSearchRepository()` 都 new 一个，意味着搜索历史在不同搜索页之间不共享。Module 3 阶段先这样，3.7 接 DataStore 后会自动解决（DataStore 是进程级单例）。

#### 步骤 5：在 UI 里获取 ViewModel

将来 3.4 实现的 `SearchScreen` 会这样用：

```kotlin
@Composable
fun SearchScreen() {
    val viewModel: SearchViewModel = viewModel(factory = SearchViewModelFactory())
    val uiState by viewModel.uiState.collectAsState()
    // 根据 uiState 渲染不同的内容
}
```

#### 思考题（不用答，但要在心里清楚）

1. 为什么用 `MutableStateFlow` 不用 `LiveData`？
2. `viewModelScope` 跟 `GlobalScope.launch` 有什么区别？为什么不能用 `GlobalScope`？
3. `debounce(300)` 如果改成 `300000`（5 分钟）会怎样？改成 `0` 又会怎样？

#### 验收

- [ ] `SearchUiState.kt`、`SearchViewModel.kt`、`SearchViewModelFactory.kt` 全部创建
- [ ] 3 个 TODO 自己填完
- [ ] 用一段临时的 Compose UI 测试 ViewModel：输入文字能看到 `uiState` 变化（在 Logcat 加日志验证）

#### 强制 check-in（与我）

完成后给我：

1. **`SearchViewModel.kt` 完整代码**（贴文字）
2. **3 个 TODO 你各自怎么填的 + 为什么这么填**
3. **你的自评**：
   - 防抖这个机制你是真懂了，还是抄通了？用 Java 语言描述一遍它的工作原理。
   - `observeKeywordInput` 这块为什么用 `Flow`，不用普通的 `if-else` 判断？

---

### 子任务 3.4：搜索前置页 UI

> 这部分是常规 Compose UI 工作，颗粒度跟 Module 2 一样，不再详细脚手架。

#### 做什么

实现搜索页主壳 `SearchScreen` + 前置态内容 `SearchPreContent`。

#### 主要构成

```
SearchScreen
├── 顶部搜索栏 (SearchTopBar)
│   ├── 返回箭头
│   ├── 输入框 (TextField，文字关键词)
│   ├── 清空按钮 (X)
│   └── "搜索"按钮
└── 内容区（根据 uiState 切换）
    ├── PreSearch → SearchPreContent
    │   ├── 4 个圆形入口（识剧/排行/上新/演员，Toast 占位）
    │   ├── 搜索历史 FlowRow（可选）
    │   ├── 猜你想搜（三列网格）
    │   └── 短剧热搜榜（三列带排名角标）
    ├── Suggesting → 3.5 实现
    └── ShowingResult → 3.6 实现
```

#### 关键技术点

**三列网格** vs **双列网格**（Module 2 用过双列）：

```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(3),  // ← 3 列
    ...
)
```

**搜索框设计**：

```kotlin
TextField(
    value = uiState.keyword,
    onValueChange = { viewModel.onKeywordChange(it) },
    placeholder = { Text("我的26岁女房客") },
    trailingIcon = {
        if (uiState.keyword.isNotEmpty()) {
            IconButton(onClick = { viewModel.onClearKeyword() }) {
                Icon(Icons.Default.Close, "清空")
            }
        }
    },
    singleLine = true,
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions = KeyboardActions(
        onSearch = { viewModel.onSearch(uiState.keyword) }
    ),
)
```

**热搜榜排名角标**（前 3 名特殊样式）：

```kotlin
@Composable
fun RankBadge(rank: Int) {
    val color = when (rank) {
        1 -> Color(0xFFE6303C)  // 红
        2 -> Color(0xFFFF8A24)  // 橙
        3 -> Color(0xFFFFD700)  // 金
        else -> Color.Gray
    }
    Text(
        text = rank.toString(),
        color = Color.White,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
            .background(color)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}
```

#### 验收

- [ ] 真机：进搜索页能看到完整前置内容
- [ ] 历史区域：能看到、能单个删除、能一键清空
- [ ] 猜你想搜：6 张三列卡片显示
- [ ] 热搜榜：前 3 名有彩色角标

---

### 子任务 3.5：搜索中态 + 关键词高亮

#### 做什么

实现 Suggesting 状态下的联想列表 + 关键词橙色高亮。

#### 关键技术点：AnnotatedString

Java 里的 `Text("末世来了")` 整段是一个颜色。如果要"末世"红色、"来了"灰色，需要 `AnnotatedString`。

```kotlin
fun buildHighlightedText(
    fullText: String,
    keyword: String,
    highlightColor: Color = Color(0xFFFF7A1A),
): AnnotatedString = buildAnnotatedString {
    if (keyword.isBlank()) {
        append(fullText)
        return@buildAnnotatedString
    }

    var startIndex = 0
    while (startIndex < fullText.length) {
        val matchIndex = fullText.indexOf(keyword, startIndex, ignoreCase = true)
        if (matchIndex == -1) {
            // 剩余部分没有匹配了
            append(fullText.substring(startIndex))
            break
        }
        // 关键词前的普通文字
        append(fullText.substring(startIndex, matchIndex))
        // 关键词本身（高亮）
        withStyle(SpanStyle(color = highlightColor)) {
            append(fullText.substring(matchIndex, matchIndex + keyword.length))
        }
        startIndex = matchIndex + keyword.length
    }
}
```

使用：

```kotlin
Text(text = buildHighlightedText(drama.title, keyword))
```

#### 联想列表两种条目

- **A 型**（带封面）：搜索图标 + 小封面 + 高亮剧名 + 类型 + 热度
- **B 型**（纯文字）：搜索图标 + 高亮文字

用 `LazyColumn`，前面几个 `item` 是 A 型，后面是 B 型。

#### 验收

- [ ] 真机：输入"末" → 等 300ms → 显示联想列表
- [ ] A 型条目带封面，B 型条目纯文字
- [ ] 关键词"末"在所有条目中橙色高亮
- [ ] 大小写不敏感（"末" 和 "末"，"a" 和 "A" 都匹配）

---

### 子任务 3.6：搜索结果页 + DramaCard 复用

#### 做什么

实现 ShowingResult 状态的 UI，**复用 Module 2 的 DramaCard**。

#### 关键决策点

**DramaCard 需要为关键词高亮做扩展吗？**

回看你 Module 2 接口设计 4 问的第 3 题——你当时讨论过 `titleContent: @Composable (() -> Unit)? = null` slot 方案，我反驳了，认为 `AnnotatedString` 参数更简单。

**现在到了真正用的时候，你来做最终判断：**

- 方案 A：`DramaCard` 加 `titleOverride: AnnotatedString? = null` 参数
- 方案 B：复制一份 DramaCard 改成 `SearchResultDramaCard`
- 方案 C：让 `Drama.title` 改成 `AnnotatedString` 类型（不推荐，数据模型不该耦合 UI）

**你选哪个？为什么？这是工程判断题，不是有标准答案的题。**

我倾向 A。理由：

1. 改动小（加一个可选参数）
2. 调用方代码清晰：剧场页传 `null`，搜索页传 `buildHighlightedText(...)`
3. 不破坏现有调用

实现：

```kotlin
@Composable
fun DramaCard(
    drama: Drama,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleOverride: AnnotatedString? = null,  // ← 新增可选参数
) {
    Column(...) {
        DramaCover(...)
        Column(...) {
            if (titleOverride != null) {
                Text(text = titleOverride, ...)
            } else {
                Text(text = drama.title, ...)
            }
            ...
        }
    }
}
```

#### 顶部分类 Tab

7 个分类：综合、漫剧、社区、影视、小说、听书、用户。

实现思路：所有分类共用同一份搜索结果（因为我们的 Mock 数据没有 category 字段）。可以在 `Drama` 里加一个 `category: String` 字段做过滤，或者简化为"分类 Tab 是装饰、所有 Tab 都显示同样结果"。

**推荐：简化处理**——分类 Tab 显示出来，切换有视觉变化，但所有 Tab 显示同样结果（在 README 里标注此为简化）。

#### 验收

- [ ] 输入"末" → 点搜索 → 看到结果页
- [ ] 结果页双列卡片，剧名"末"字橙色高亮
- [ ] 顶部分类 Tab 显示，可切换（视觉反馈即可）
- [ ] 输入框 X 按钮点击 → 回到前置态

---

### 子任务 3.7：DataStore 持久化历史

#### 做什么

把 `MockSearchRepository` 内存版的历史记录改成 DataStore 持久化。

#### 依赖

```kotlin
implementation("androidx.datastore:datastore-preferences:1.0.0")
```

#### 步骤

新建 `DataStoreSearchHistory.kt`，把 `MockSearchRepository` 里 `_history` 相关代码替换成 DataStore 调用。

代码量不大，但涉及协程 + Flow 转换。具体我**不在这给完整脚手架**——你 3.3 学会了 ViewModel + Flow 之后，这个会自然写出来。

**关键 API**：
- `Context.dataStore`（顶层声明 `val Context.dataStore by preferencesDataStore("search")`）
- `dataStore.data.map { it[KEY] }`（读）
- `dataStore.edit { it[KEY] = value }`（写）

历史记录是 `List<String>`，可以用分隔符拼成 String 存。

#### 验收

- [ ] 真机：搜索几次 → 杀进程重启 App → 历史记录还在
- [ ] 删除/清空操作有效
- [ ] 进程级单例：不管 SearchScreen 创建几次，历史都一致

---

### 子任务 3.8：Module 3 汇总同步 [强制 check-in]

#### 做什么

跟我做完整的 Module 3 汇总同步。按我们的协议，你交付：

1. **另一个 Claude review 通过的确认 + 它发现的关键 bug 列表**
2. **关键决策与代码片段**：
   - SearchRepository 接口（4 问回答）
   - SearchViewModel 的 Flow 链
   - DramaCard 复用方案（A/B/C 你选了哪个 + 为什么）
3. **卡点和解决**：Module 3 有 7 个子任务，你具体在哪卡过？怎么解决的？
4. **自我评估**（必填，最重要）：
   - 防抖、StateFlow、Repository 模式，哪个让你最不踏实？
   - 你画像里写"看代码不玩代码"——Module 3 你具体在哪几个地方做了"玩代码"（改一改、跑一跑、看结果）？
   - 如果让你不看任何代码、不查文档，从零写一个"输入框带防抖的搜索功能"，你能写多少？写不出来的部分是哪些？

---

## 3. 整体验收（PRD §3.4 标准）

**搜索前置页**：
- [ ] 4 个圆形入口、猜你想搜（3 列）、热搜榜（3 列带排名）UI 完整
- [ ] 历史记录正常显示，可单条删除、可清空、可点击搜索
- [ ] **App 重启后历史记录仍然存在**
- [ ] 同一关键词重复搜索不在历史中重复，最新的排最前

**搜索中态**：
- [ ] 输入文字时实时显示联想列表
- [ ] **实现了 300ms 防抖**（连续打字不卡，停止打字才触发联想）
- [ ] 关键词在联想结果中橙色高亮
- [ ] 联想列表中既有带封面的剧目，也有纯文字

**搜索结果页**：
- [ ] 顶部分类 Tab 可切换，视觉反馈正常
- [ ] 双列卡片**复用了 DramaCard 组件**（不是复制一份）
- [ ] 结果中关键词橙色高亮
- [ ] 搜索框 X 清空 → 回到前置/中态

**整体**：
- [ ] 三态切换流畅，无卡顿、无闪烁
- [ ] Git commits 清晰

---

## 4. Module 3 完成后你应该掌握的

工程心智：
- [ ] 知道什么是 Repository 模式，能解释为什么 UI 不应该直接调数据源
- [ ] 知道什么是 ViewModel，能解释它跟 Activity / Composable 生命周期的关系
- [ ] 能用一段 Java 伪代码描述 `Flow.debounce` 的工作原理
- [ ] 知道什么时候用 `Flow`、什么时候用 `suspend fun`

代码能力：
- [ ] 能从零写一个 ViewModel + StateFlow + Compose collectAsState 的小例子
- [ ] 能在 Compose 里实现简单的状态机（sealed interface + when 分支）
- [ ] 能用 AnnotatedString 实现富文本

工程协作：
- [ ] 跟另一个 Claude review + 跟我做架构 check-in 的协作模式跑通

---

**接下来**：从子任务 3.1 开始，先把 §1 读完。**§1 不是装饰，是必读基础**。

读完 §1，开始 3.1。完成 3.2 强制 check-in 1，完成 3.3 强制 check-in 2，完成 Module 3 强制汇总同步。

去做吧。
