package com.example.hongguolite.ui.screens.theater

import androidx.lifecycle.ViewModel
import com.example.hongguolite.data.mock.MockDramas
import com.example.hongguolite.data.model.Drama
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TheaterUiState(
    val dramas: List<Drama> = emptyList()
)

class TheaterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TheaterUiState())
    val uiState: StateFlow<TheaterUiState> = _uiState.asStateFlow()

    init {
        loadDramas()
    }

    private fun loadDramas() {
        // ViewModel 负责获取数据并更新状态
        _uiState.value = TheaterUiState(dramas = MockDramas.theaterList)
    }
}

