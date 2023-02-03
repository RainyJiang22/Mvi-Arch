package com.base.mvi_arch.ui.travel.state

import com.base.mvi_arch.data.TravelItem

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */
sealed class TravelTabViewState {

    object LoadingState : TravelTabViewState()

    data class RefreshSuccess(val travelList: List<TravelItem>) : TravelTabViewState()

    data class LoadMoreSuccess(val travelList: List<TravelItem>) : TravelTabViewState()

    data class LoadError(val errorMsg: String) : TravelTabViewState()
}