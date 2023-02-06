package com.base.mvi_arch.ui.video.state

import com.base.mvi_arch.data.VideoModel

/**
 * @author jiangshiyu
 * @date 2023/2/6
 */
sealed class VideoListViewState {


    object LoadingState: VideoListViewState()

    data class RefreshSuccessState(val videoList: List<VideoModel>) : VideoListViewState()

    data class LoadMoreSuccessState(val videoList: List<VideoModel>) : VideoListViewState()

    data class LoadErrorState(val errorMsg: String): VideoListViewState()

    data class LoadMoreErrorState(val errorMsg: String): VideoListViewState()
}