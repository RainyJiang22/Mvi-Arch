package com.base.mvi_arch.ui.travel.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.base.mvi_arch.data.TravelResponse
import com.base.mvi_arch.network.KtorClient
import com.base.mvi_arch.network.TravelApiService
import com.base.mvi_arch.network.URLS
import com.base.mvi_arch.ui.travel.action.TravelViewAction
import com.base.mvi_arch.ui.travel.state.TravelViewState
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jzvd.jzvideo.TAG

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */

class TravelViewModel(application: Application) : AndroidViewModel(application) {


    companion object {
        const val TAG = "TravelViewModel"
    }

    private val _state = MutableStateFlow<TravelViewState>(TravelViewState.LoadingState)
    val state: StateFlow<TravelViewState>
        get() = _state


    private val userIntent = MutableSharedFlow<TravelViewAction>()


    init {
        viewModelScope.launch {
            userIntent.collect { viewAction ->
                when (viewAction) {
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
            try {
                val response: String = KtorClient.client.get(URLS.TRAVEL_TAB_URL).body()
                withContext(Dispatchers.Main) {
                    // 在主线程上处理响应数据
                    Log.i(TAG, "Response: $response")
//                    _state.value = TravelViewState.LoadSuccess(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "Error: ${e.message}", e)
                    _state.value = TravelViewState.LoadFail(e.message.toString())
                }
            }
        }
//        viewModelScope.launch {
//            kotlin.runCatching {
//                TravelApiService.getTravelTab()
//            }.onSuccess {
//                _state.value = TravelViewState.LoadSuccess(it)
//            }.onFailure {
//                _state.value = TravelViewState.LoadFail(it.message.toString())
//            }
//        }
    }


}