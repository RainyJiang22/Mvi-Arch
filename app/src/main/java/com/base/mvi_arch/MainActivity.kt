package com.base.mvi_arch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.base.mvi_arch.base.BaseActivity
import com.base.mvi_arch.base.EmptyViewModel
import com.base.mvi_arch.databinding.ActivityMainBinding
import com.base.mvi_arch.login.LoginViewEvent
import com.base.mvi_arch.login.LoginViewModel
import com.drake.serialize.intent.bundle

class MainActivity : BaseActivity<ActivityMainBinding, EmptyViewModel>() {


    private val username: String by bundle()
    private val password: String by bundle()

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
    }
}