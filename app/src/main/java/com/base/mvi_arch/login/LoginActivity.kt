package com.base.mvi_arch.login

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.base.mvi_arch.MainActivity
import com.base.mvi_arch.base.BaseActivity
import com.base.mvi_arch.databinding.ActivityLoginBinding
import com.base.mvi_arch.register.RegisterActivity
import com.base.mvi_arch.toast
import com.base.mvi_core.observeEvent
import com.base.mvi_core.observeState
import com.drake.serialize.intent.openActivity
import com.drake.statelayout.state

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        initView()
        initViewStates()
        initViewEvents()
    }


    private fun initView() {
        binding?.editUserName?.addTextChangedListener {
            viewModel.dispatch(LoginViewAction.UpdateUserName(it.toString()))
        }

        binding?.editPassword?.addTextChangedListener {
            viewModel.dispatch(LoginViewAction.UpdatePassword(it.toString()))
        }

        binding?.btnLogin?.setOnClickListener {
            viewModel.dispatch(LoginViewAction.Login)
        }

        binding?.tvRegister?.setOnClickListener {
            openActivity<RegisterActivity>()
            finishAfterTransition()
        }
    }

    private fun initViewStates() {
        viewModel.viewStates.let { states ->
            states.observeState(this, LoginViewState::userName) {
                binding?.editUserName?.setText(it)
                binding?.editUserName?.setSelection(it.length)
            }
            states.observeState(this, LoginViewState::passWord) {
                binding?.editPassword?.setText(it)
                binding?.editPassword?.setSelection(it.length)
            }

            states.observeState(this, LoginViewState::isLoginEnable) {
                binding?.btnLogin?.isEnabled = it
                binding?.btnLogin?.alpha = if (it) 1f else 0.5f
            }

            states.observeState(this, LoginViewState::passwordTipVisible) {
                binding?.tvLabel?.isVisible = it
            }
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvents.observeEvent(this) {
            when (it) {
                is LoginViewEvent.ShowLoadingDialog -> showLoadingDialog()
                is LoginViewEvent.DismissLoadingDialog -> dismissLoadingDialog()
                is LoginViewEvent.ShowToast -> toast(it.message)
                is LoginViewEvent.LoginSuc -> {
                    toast(it.userInfoResponse.toString())
                    openActivity<MainActivity>(
                        "user" to it.userInfoResponse.username,
                        "password" to it.userInfoResponse.password
                    )
                }
            }
        }
    }

    //test
    private var progressDialog: ProgressDialog? = null

    private fun showLoadingDialog() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(this)
        progressDialog?.show()
    }

    private fun dismissLoadingDialog() {
        progressDialog?.takeIf { it.isShowing }?.dismiss()
    }
}