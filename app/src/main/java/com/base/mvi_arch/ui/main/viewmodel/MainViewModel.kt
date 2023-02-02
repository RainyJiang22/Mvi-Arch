package com.base.mvi_arch.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.base.mvi_arch.ui.main.action.MainViewAction
import com.base.mvi_arch.ui.main.state.MainViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val savedHandle: SavedStateHandle = SavedStateHandle()
        private const val SELECT_TAB_INDEX = "selected_tab_index"
    }


    private val _state = MutableStateFlow<MainViewState>(MainViewState.InitialDefaultTab(0))
    val state: StateFlow<MainViewState>
        get() = _state

    private val userIntent = MutableSharedFlow<MainViewAction>()

    init {
        viewModelScope.launch {
            userIntent.collect { viewAction ->
                when (viewAction) {
                    is MainViewAction.GetCurrentTabIndex -> getDefaultTabSelectedIndex()
                    is MainViewAction.SaveCurrentTabIndex -> saveTabSelectedIndex(viewAction.index)
                }
            }
        }
    }

    fun dispatch(viewAction: MainViewAction) {
        viewModelScope.launch {
            userIntent.emit(viewAction)
        }
    }

    private fun getDefaultTabSelectedIndex() {
        val index = savedHandle.get<Int>(SELECT_TAB_INDEX) ?: 0
        _state.value = MainViewState.InitialDefaultTab(index)
    }

    private fun saveTabSelectedIndex(index: Int) {
        savedHandle[SELECT_TAB_INDEX] = index
        _state.value = MainViewState.InitialDefaultTab(index)
    }


}