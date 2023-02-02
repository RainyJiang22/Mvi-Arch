package com.base.mvi_arch.ui.register

import com.base.mvi_arch.data.UserInfoResponse

/**
 * @author jiangshiyu
 * @date 2022/6/17
 */


data class RegisterViewState(
    val userName: String = "",
    val password: String = "",
    val rePassword: String = ""
) {
    val isRegisterEnable: Boolean
        get() = userName.isNotEmpty() && password.length >= 6
                && password == rePassword && rePassword.isNotEmpty()

    val passwordTip: Boolean
        get() = password.length in 1..5

    val rePasswordTip: Boolean
        get() = password != rePassword && password.isNotEmpty() && rePassword.isNotEmpty()
}

sealed class RegisterViewEvent {

    data class RegisterSuc(val userInfoResponse: UserInfoResponse) : RegisterViewEvent()
    data class ShowToast(val message: String) : RegisterViewEvent()
    object ShowLoadingDialog : RegisterViewEvent()
    object DismissLoadingDialog : RegisterViewEvent()
}

sealed class RegisterViewAction {

    data class UpdateUserName(val userName: String) : RegisterViewAction()
    data class UpdatePassword(val passWord: String) : RegisterViewAction()
    data class UpdateRePassword(val rePassword: String) : RegisterViewAction()
    object Register : RegisterViewAction()
}



