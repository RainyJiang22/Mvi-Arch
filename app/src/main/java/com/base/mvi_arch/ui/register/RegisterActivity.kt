package com.base.mvi_arch.ui.register

import android.app.ProgressDialog
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.base.mvi_arch.ui.main.MainActivity
import com.base.mvi_arch.base.BaseActivity
import com.base.mvi_arch.databinding.ActivityRegisterBinding
import com.base.mvi_arch.toast
import com.base.mvi_core.observeEvent
import com.base.mvi_core.observeState
import com.drake.serialize.intent.openActivity

/**
 * @author jiangshiyu
 * @date 2022/6/17
 */
class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterViewModel>() {
    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        initView()
        initViewStates()
        initViewEvents()
    }


    private fun initView() {
        binding?.editUserName?.addTextChangedListener {
            viewModel.dispatch(RegisterViewAction.UpdateUserName(it.toString()))
        }
        binding?.editPassword?.addTextChangedListener {
            viewModel.dispatch(RegisterViewAction.UpdatePassword(it.toString()))
        }
        binding?.editRePassword?.addTextChangedListener {
            viewModel.dispatch(RegisterViewAction.UpdateRePassword(it.toString()))
        }
        binding?.btnRegister?.setOnClickListener {
            viewModel.dispatch(RegisterViewAction.Register)
        }
    }

    private fun initViewStates() {
        viewModel.viewStates.let { states ->
            states.observeState(this, RegisterViewState::userName) {
                binding?.editUserName?.setText(it)
                binding?.editUserName?.setSelection(it.length)
            }
            states.observeState(this, RegisterViewState::password) {
                binding?.editPassword?.setText(it)
                binding?.editPassword?.setSelection(it.length)
            }

            states.observeState(this, RegisterViewState::rePassword) {
                binding?.editRePassword?.setText(it)
                binding?.editRePassword?.setSelection(it.length)
            }

            states.observeState(this, RegisterViewState::isRegisterEnable) {
                binding?.btnRegister?.isEnabled = it
                binding?.btnRegister?.alpha = if (it) 1f else 0.5f
            }

            states.observeState(this, RegisterViewState::passwordTip) {
                binding?.tvLabel?.isVisible = it
            }

            states.observeState(this, RegisterViewState::rePasswordTip) {
                binding?.tvReLabel?.isVisible = it
            }
        }
    }

    private fun initViewEvents() {

        viewModel.viewEvents.observeEvent(this) {
            when (it) {
                is RegisterViewEvent.ShowToast -> toast(it.message)
                is RegisterViewEvent.ShowLoadingDialog -> showLoadingDialog()
                is RegisterViewEvent.DismissLoadingDialog -> dismissLoadingDialog()
                is RegisterViewEvent.RegisterSuc -> {
                    openActivity<MainActivity>()
                }
            }
        }
    }

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