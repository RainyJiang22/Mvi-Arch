package com.base.mvi_arch.login

import com.base.mvi_arch.data.UserInfoResponse

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
data class LoginViewState(val userName: String = "", val passWord: String = "") {
    val isLoginEnable: Boolean
        get() = userName.isNotEmpty() && passWord.length >= 6

    val passwordTipVisible: Boolean
        get() = passWord.length in 1..5
}

sealed class LoginViewEvent {
    data class ShowToast(val message: String) : LoginViewEvent()

    object ShowLoadingDialog : LoginViewEvent()

    object DismissLoadingDialog : LoginViewEvent()

    data class LoginSuc(val userInfoResponse: UserInfoResponse) : LoginViewEvent()
}

sealed class LoginViewAction {

    data class UpdateUserName(val userName: String) : LoginViewAction()
    data class UpdatePassword(val passWord: String) : LoginViewAction()
    object Login : LoginViewAction()
}


