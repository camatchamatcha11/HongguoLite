package com.example.hongguolite.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hongguolite.data.model.Drama
import com.example.hongguolite.data.repository.SearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
/**
 * 搜索页面的 ViewModel，负责管理搜索页的三态（前置、联想、结果）及用户输入流。
 *
 * 采用 MVI/MVVM 架构，所有 UI 状态通过 [uiState] 对外暴露，UI 层仅发出意图（如 [onKeywordChange]）。
 * 包括了防抖输入、搜索历史管理、猜你想搜与热搜榜数据加载等功能。
 */
class SearchViewModel(
    private val repository: SearchRepository,
    private val debounceMillis: Long = 300L,
    externalScope: CoroutineScope? = null,
) : ViewModel() {
    // 测试时传 externalScope，可以避开 Android Main Dispatcher；真 App 中默认使用 viewModelScope。
    private val scope = externalScope ?: viewModelScope

    // 输入框的原始输入流：用户每输入一个字都会立刻写进这里。
    private val keywordInput = MutableStateFlow("")

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.PreSearch())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    // 前置页基础数据单独缓存，避免历史记录变化时丢掉“猜你想搜”和“热搜榜”。
    private var guessLikes: List<Drama> = emptyList()
    private var hotSearches: List<Drama> = emptyList()
    private var history: List<String> = emptyList()

    /**
     * 初始化时加载前置页数据（包括猜你想搜、热搜等）并观察输入流及历史记录流。
     */
    init {
        loadPreSearchContent()
        observeKeywordInput()
        observeHistory()
    }

    /** 加载前置页内容（猜你想搜 + 热搜） */
    private fun loadPreSearchContent() {
        scope.launch {
            guessLikes = repository.getGuessLikes()
            hotSearches = repository.getHotSearches()
            showPreSearch(keyword = keywordInput.value)
        }
    }

    /** 观察用户输入 + 防抖 + 触发联想 */
    private fun observeKeywordInput() {
        scope.launch {
            keywordInput
                .debounce(debounceMillis)
                .distinctUntilChanged()
                .collectLatest { keyword ->
                    val normalizedKeyword = keyword.trim()
                    if (normalizedKeyword.isBlank()) {
                        showPreSearch(keyword = "")
                    } else {
                        val currentState = _uiState.value
                        if (currentState is SearchUiState.ShowingResult && currentState.keyword == normalizedKeyword) {
                            return@collectLatest
                        }
                        // collectLatest 会取消旧关键词的计算，保证旧结果不会覆盖新输入。
                        val dramas =
                            repository.searchDramas(normalizedKeyword).take(DRAMA_SUGGESTION_LIMIT)
                        val keywords = repository.getSuggestions(normalizedKeyword)
                        _uiState.value = SearchUiState.Suggesting(
                            keyword = normalizedKeyword,
                            dramaSuggestions = dramas,
                            keywordSuggestions = keywords,
                        )
                    }
                }
        }
    }

    private fun observeHistory() {
        scope.launch {
            repository.getSearchHistory().collectLatest { newHistory ->
                history = newHistory
                if (_uiState.value is SearchUiState.PreSearch) {
                    showPreSearch(keyword = keywordInput.value)
                }
            }
        }
    }

    /**
     * 当用户在搜索框输入文字时触发，更新关键词流并在输入框非空时切换为 [SearchUiState.Suggesting] 状态。
     *
     * @param keyword 最新的搜索关键词。
     */
    fun onKeywordChange(keyword: String) {
        keywordInput.value = keyword

        if (keyword.isBlank()) {
            showPreSearch(keyword = "")
        } else {
            // 输入框要立即显示新文字；真正的联想列表等 debounce 后再刷新。
            _uiState.value = SearchUiState.Suggesting(keyword = keyword)
        }
    }

    /**
     * 用户点击搜索或选择联想词时触发，执行实际搜索并切换状态至 [SearchUiState.ShowingResult]。
     *
     * @param keyword 要搜索的关键词，默认为当前输入框中的文本。
     */
    fun onSearch(keyword: String = uiState.value.keyword) {
        val normalizedKeyword = keyword.trim()
        if (normalizedKeyword.isBlank()) return

        // 搜索按钮代表用户已经确认关键词，因此这里同步输入流，避免旧的空输入防抖结果
        // 在搜索完成后又把页面切回前置态。
        keywordInput.value = normalizedKeyword

        scope.launch {
            _uiState.value = SearchUiState.ShowingResult(
                keyword = normalizedKeyword,
                isLoading = true,
            )
            repository.addSearchHistory(normalizedKeyword)
            val results = repository.searchDramas(normalizedKeyword)
            _uiState.value = SearchUiState.ShowingResult(
                keyword = normalizedKeyword,
                results = results,
                isLoading = false,
            )
        }
    }

    /**
     * 清空搜索框输入，并退回到前置态。
     */
    fun onClearKeyword() {
        keywordInput.value = ""
        showPreSearch(keyword = "")
    }

    /**
     * 删除单条搜索历史记录。
     *
     * @param keyword 要删除的历史搜索词。
     */
    fun onDeleteHistory(keyword: String) {
        scope.launch {
            repository.removeSearchHistory(keyword)
        }
    }

    /**
     * 清空全部搜索历史。
     */
    fun onClearAllHistory() {
        scope.launch {
            repository.clearSearchHistory()
        }
    }

    private fun showPreSearch(keyword: String) {
        _uiState.value = SearchUiState.PreSearch(
            keyword = keyword,
            guessLikes = guessLikes,
            hotSearches = hotSearches,
            history = history,
        )
    }

    private companion object {
        const val DRAMA_SUGGESTION_LIMIT = 3
    }
}
