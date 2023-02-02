package com.base.mvi_arch.ui.login

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


//    fixme StateFlow与SharedFlow
//     关于SharedFlow
//     它不同与标准flow的地方,两个区别 （1） 是热流实现，即使不调用collect也会产生事件 (2)可以有多个订阅者
//     mutableShareFlow有三个参数
//     replay：向新订阅者重放的数值的数量。它不能是负数，默认为零。
//     extraBufferCapacity：缓冲的值的数量。不能为负数，默认为零。这个值加上replay的总和，构成了SharedFlow的总缓冲区大小。
//     onBufferOverflow（缓冲区溢出）：达到缓冲区溢出时采取的行为。它可以有三个值：BufferOverflow.SUSPEND, BufferOverflow.DROP_OLDEST或BufferOverflow.DROP_LATEST。它的默认值是BufferOverflow.SUSPEND
//     关于 StateFlow
//     stateFlow可以理解为它是一个特殊化的shareFlow的子类,它更多的是做一个状态管理
//     尽量避免在stateFlow中使用emit/tryemit,意味着更新速度超过了订阅者的消费速度，最终只能得到最新的值
//     需要注意的是无论什么时候，上一个值需要和更新的值不同，否者无法更新，stateFlow做的就是和这个类似`distinctUntilChanged`
//     如果你有某种状态管理，你可以使用StateFlow。
//     只要你有一些事件流在进行，如果事件没有被所有可能的订阅者处理，或者过去的事件可能根本没有被处理，都不是问题，你可以使用SharedFlow
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