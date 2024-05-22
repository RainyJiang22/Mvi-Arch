package com.base.mvi_arch.ui.travel.activity

import android.os.Bundle
import android.view.KeyEvent
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.mvi_arch.base.BaseActivity
import com.base.mvi_arch.base.EmptyViewModel
import com.base.mvi_arch.databinding.ActivityTravelDetailBinding
import com.base.mvi_arch.event.GlobalEvent
import com.biubiu.eventbus.post.postEvent
import com.just.agentweb.AgentWeb

/**
 * @author jiangshiyu
 * @date 2023/2/5
 */
class TravelDetailActivity : BaseActivity<ActivityTravelDetailBinding, EmptyViewModel>() {

    companion object {
        const val KEY_TITLE = "key_title"
        const val KEY_URL = "key_url"
    }

    private lateinit var mAgentWeb: AgentWeb

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        initView()
        initWindow()
    }

    private fun initWindow() {
        WindowCompat.getInsetsController(window, findViewById(android.R.id.content)).apply {
            isAppearanceLightStatusBars = true
        }
    }

    private fun initView() {
        binding?.toolbar?.apply {
            title = intent.getStringExtra(KEY_TITLE)
          //  setSupportActionBar(this)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding!!.llContent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(intent.getStringExtra(KEY_URL))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}
