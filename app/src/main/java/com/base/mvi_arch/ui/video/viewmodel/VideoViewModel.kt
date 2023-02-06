package com.base.mvi_arch.ui.video.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.base.mvi_arch.network.VideoApi
import com.base.mvi_arch.network.VideoApiService
import com.base.mvi_arch.ui.video.action.VideoListViewAction
import com.base.mvi_arch.ui.video.state.VideoListViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2023/2/6
 */
class VideoViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<VideoListViewState>(VideoListViewState.LoadingState)
    val state: StateFlow<VideoListViewState>
        get() = _state

    private val userIntent = MutableSharedFlow<VideoListViewAction>()


    init {
        //处理用户事件
        viewModelScope.launch {
            userIntent.collect {
                when (it) {
                    is VideoListViewAction.Refresh -> getVideoList()
                    is VideoListViewAction.Retry -> retry()
                    is VideoListViewAction.LoadMore -> loadMore(it.page)
                }
            }
        }
    }

    private fun refresh() {
        getVideoList()
    }

    private fun retry() {
        _state.value = VideoListViewState.LoadingState
        getVideoList()
    }

    private fun loadMore(page: Int) {
        getVideoList(page)
    }


    private fun getVideoList(page: Int = 0) {
        viewModelScope.launch {
            kotlin.runCatching {
                VideoApiService.getVideoList(page = page)
            }.onSuccess {
                _state.value =
                    if (page == 0)
                        VideoListViewState.RefreshSuccessState(it.result.list)
                    else
                        VideoListViewState.LoadMoreSuccessState(it.result.list)
            }.onFailure {
                _state.value =
                    if (page == 0)
                        VideoListViewState.LoadErrorState(it.message.toString())
                    else
                        VideoListViewState.LoadErrorState(it.message.toString())
            }
        }
    }

    /**
     * 分发用户事件
     */
    fun dispatch(viewAction: VideoListViewAction) {
        viewModelScope.launch {
            userIntent.emit(viewAction)
        }
    }
}