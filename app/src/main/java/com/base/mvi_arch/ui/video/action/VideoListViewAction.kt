package com.base.mvi_arch.ui.video.action

/**
 * @author jiangshiyu
 * @date 2023/2/6
 */
sealed class VideoListViewAction {

    object Refresh : VideoListViewAction()

    object Retry : VideoListViewAction()

    data class LoadMore(val page: Int) : VideoListViewAction()

}