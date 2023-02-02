package com.base.mvi_arch.ui.main.state

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
sealed class MainViewState {

    //当前选中的Tab状态
    data class InitialDefaultTab(val index: Int) : MainViewState()

}