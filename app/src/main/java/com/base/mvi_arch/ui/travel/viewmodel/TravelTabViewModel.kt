package com.base.mvi_arch.ui.travel.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.base.mvi_arch.data.Params
import com.base.mvi_arch.data.VideoListModel
import com.base.mvi_arch.network.TravelApi
import com.base.mvi_arch.network.TravelApiService
import com.base.mvi_arch.ui.travel.action.TravelTabViewAction
import com.base.mvi_arch.ui.travel.state.TravelTabViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */
class TravelTabViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * muTABLE
     */
    /**
     *  MutableStateFlow  侧重状态 (State),状态可以是的 UI 组件的可见性，它始终具有一个值（显示/隐藏）
     *  MutableSharedFlow 侧重事件(Event),事件只有在满足一个或多个前提条件时才会触发，不需要也不应该有默认值
     */
    private val _state = MutableStateFlow<TravelTabViewState>(TravelTabViewState.LoadingState)
    val state: StateFlow<TravelTabViewState>
        get() = _state

    private val userIntent = MutableSharedFlow<TravelTabViewAction>()


    init {
        //处理用户事件
        viewModelScope.launch {
            userIntent.collect { viewAction ->
                when (viewAction) {
                    is TravelTabViewAction.Refresh -> {
                        getTravelCategoryList(viewAction.url, viewAction.param)
                    }

                    is TravelTabViewAction.LoadMore -> {
                        getTravelCategoryList(viewAction.url, viewAction.param)
                    }
                }
            }
        }
    }

    fun dispatch(viewAction: TravelTabViewAction) {
        viewModelScope.launch {
            userIntent.emit(viewAction)
        }
    }


    private fun getTravelCategoryList(url: String, params: Params) {
        viewModelScope.launch {
            kotlin.runCatching {
                TravelApiService.getTravelCategoryList(url, params)
            }.onSuccess {
                _state.emit(
                    if (params.pagePara.pageIndex == 0) TravelTabViewState.RefreshSuccess(it.resultList)
                    else TravelTabViewState.LoadMoreSuccess(it.resultList)
                )
            }.onFailure {
                _state.emit(TravelTabViewState.LoadError(it.message.toString()))
            }
        }
    }

}