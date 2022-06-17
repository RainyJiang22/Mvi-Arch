package com.base.mvi_arch.register

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
 * @date 2022/6/17
 */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WanRepository = WanRepository.getInstance()
    private val _viewStates = MutableStateFlow(RegisterViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<RegisterViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()


    fun dispatch(viewAction: RegisterViewAction) {
        when (viewAction) {
            is RegisterViewAction.UpdateUserName -> updateUserName(viewAction.userName)
            is RegisterViewAction.UpdatePassword -> updatePassword(viewAction.passWord)
            is RegisterViewAction.UpdateRePassword -> updateRePassword(viewAction.rePassword)
            is RegisterViewAction.Register -> register()
        }
    }


    private fun updateUserName(userName: String) {
        _viewStates.setState { copy(userName = userName) }
    }

    private fun updatePassword(password: String) {
        _viewStates.setState { copy(password = password) }
    }

    private fun updateRePassword(rePassword: String) {
        _viewStates.setState { copy(rePassword = rePassword) }
    }


    private fun register() {
        viewModelScope.launch {
            flow {
                emit(registerLogic())
            }.onStart {
                _viewEvents.setEvent(RegisterViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(
                    RegisterViewEvent.RegisterSuc(it),
                    RegisterViewEvent.DismissLoadingDialog,
                    RegisterViewEvent.ShowToast("注册成功")
                )
            }.catch {
                _viewStates.setState { copy(password = "", rePassword = "") }
                _viewEvents.setEvent(
                    RegisterViewEvent.DismissLoadingDialog,
                    RegisterViewEvent.ShowToast("注册失败")
                )
            }.collect()
        }
    }


    private suspend fun registerLogic(): UserInfoResponse {
        withState(viewStates) {
            Log.d("Register", "registerLogic:${it.userName},${it.password},${it.rePassword}")
            when (val result =
                repository.getRegisterResponse(it.userName, it.password, it.rePassword)) {

                is PageState.Fail -> {
                    //注册失败
                    throw Exception("注册失败")
                }

                is PageState.Success -> {
                    return result.data
                }
            }
        }
    }


}