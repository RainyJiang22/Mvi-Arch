package com.base.mvi_arch.ui.travel.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.base.mvi_arch.network.TravelApiService
import com.base.mvi_arch.ui.travel.action.TravelViewAction
import com.base.mvi_arch.ui.travel.state.TravelViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */

class TravelViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<TravelViewState>(TravelViewState.LoadingState)
    val state: StateFlow<TravelViewState>
        get() = _state


    private val userIntent = MutableSharedFlow<TravelViewAction>()


    init {
       viewModelScope.launch {
           userIntent.collect { viewAction->
               when(viewAction) {
                   is TravelViewAction.GetTravelTabs -> getTravelTabs()
                   is TravelViewAction.Retry -> retry()
               }
           }
       }
    }


    fun dispatch(viewAction: TravelViewAction) {
        viewModelScope.launch {
            userIntent.emit(viewAction)
        }
    }

    private fun retry() {
        _state.value = TravelViewState.LoadingState
        getTravelTabs()
    }

    /**
     * 获取Tab数据
     */
    private fun getTravelTabs() {
        viewModelScope.launch {
            kotlin.runCatching {
                TravelApiService.getTravelTab()
            }.onSuccess {
                _state.value = TravelViewState.LoadSuccess(it)
            }.onFailure {
                _state.value = TravelViewState.LoadFail(it.message.toString())
            }
        }
    }


}