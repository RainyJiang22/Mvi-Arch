package com.base.mvi_arch.network

import com.base.mvi_arch.PageState
import com.base.mvi_arch.data.ApiResponse
import com.base.mvi_arch.data.UserInfoResponse
import kotlinx.coroutines.delay

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class WanRepository {


    companion object {
        //singleton
        @Volatile
        private var instance: WanRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: WanRepository().also { instance = it }
            }
    }


    //登录
    suspend fun getLoginResponse(
        userName: String,
        password: String
    ): PageState<UserInfoResponse> {

        val loginResult = try {
            delay(2000)
            WanApi.create().login(userName, password)
        } catch (e: Exception) {
            return PageState.Fail(e)
        }

        loginResult.data?.let {
            return PageState.Success(data = it)
        } ?: run {
            return PageState.Fail("login fail, the account is not exist")
        }
    }

    //注册
    suspend fun getRegisterResponse(
        userName: String,
        password: String,
        rePassword: String,
    ): PageState<UserInfoResponse> {

        val registerResult = try {
            delay(2000)
            WanApi.create().register(userName, password, rePassword)
        } catch (e: Exception) {
            return PageState.Fail(e)
        }

        registerResult.data?.let {
            return PageState.Success(data = it)
        } ?: run {
            return PageState.Fail("register fail")
        }
    }


}