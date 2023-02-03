package com.base.mvi_arch.ui.travel.action

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */
sealed class TravelViewAction {

    object GetTravelTabs : TravelViewAction()

    object Retry : TravelViewAction()
}
