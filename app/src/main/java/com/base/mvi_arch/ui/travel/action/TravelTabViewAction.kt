package com.base.mvi_arch.ui.travel.action

import com.base.mvi_arch.data.Params

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */
sealed class TravelTabViewAction {

    data class Refresh(val url: String, val param: Params) : TravelTabViewAction()

    data class LoadMore(val url: String, val param: Params) : TravelTabViewAction()
}
