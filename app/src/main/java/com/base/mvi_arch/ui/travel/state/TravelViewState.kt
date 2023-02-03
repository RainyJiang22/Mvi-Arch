package com.base.mvi_arch.ui.travel.state

import com.base.mvi_arch.data.TravelResponse

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */
sealed class TravelViewState {

    object LoadingState : TravelViewState()

    data class LoadSuccess(val travelViewResponse: TravelResponse) : TravelViewState()

    data class LoadFail(val errorMsg: String) : TravelViewState()

}
