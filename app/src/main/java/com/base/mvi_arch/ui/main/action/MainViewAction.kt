package com.base.mvi_arch.ui.main.action

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
sealed class MainViewAction {

    //用户行为
    object GetCurrentTabIndex : MainViewAction()

    data class SaveCurrentTabIndex(val index: Int) : MainViewAction()

}