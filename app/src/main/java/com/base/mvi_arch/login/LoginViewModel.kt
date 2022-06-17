package com.base.mvi_arch.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.base.mvi_arch.PageState
import com.base.mvi_arch.data.UserInfoResponse
import com.base.mvi_arch.network.WanRepository
import com.base.mvi_core.SharedFlowEvents
import com.base.mvi_core.setEvent
import com.base.mvi_core.setState
import com.base.mvi_core.withState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WanRepository = WanRepository.getInstance()
    private val _viewStates = MutableStateFlow(LoginViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<LoginViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()


    fun dispatch(viewAction: LoginViewAction) {
        when (viewAction) {
            is LoginViewAction.UpdateUserName -> updateUserName(viewAction.userName)
            is LoginViewAction.UpdatePassword -> updatePassword(viewAction.passWord)
            is LoginViewAction.Login -> login()
        }
    }

    private fun updateUserName(userName: String) {
        _viewStates.setState { copy(userName = userName) }
    }

    private fun updatePassword(passWord: String) {
        _viewStates.setState { copy(passWord = passWord) }
    }


    private fun login() {
        //在一个SharedFlow上调用Flow.collect()时，不是在收集它的所有事件。相反，订阅的是在该订阅存在时被发出的事件
        viewModelScope.launch {
            flow {
                emit(loginLogic())
            }.onStart {
                _viewEvents.setEvent(LoginViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(
                    LoginViewEvent.DismissLoadingDialog,
                    LoginViewEvent.ShowToast("登录成功"),
                    LoginViewEvent.LoginSuc(it)
                )
            }.catch {
                _viewStates.setState { copy(userName = "", passWord = "") }
                _viewEvents.setEvent(
                    LoginViewEvent.DismissLoadingDialog, LoginViewEvent.ShowToast("登录失败")
                )
            }.collect()
        }
    }

    private suspend fun loginLogic(): UserInfoResponse {
        withState(viewStates) {
            Log.d("Login", "loginLogic: ${it.userName},${it.passWord}")
            when (val result = repository.getLoginResponse(it.userName, it.passWord)) {
                is PageState.Fail -> {
                    //登录失败
                    throw Exception("登录失败")
                }

                is PageState.Success -> {
                    //登录成功
                    return result.data
                }
            }
        }
    }


}