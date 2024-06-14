package com.base.mvi_arch.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import arrow.core.Either
import arrow.core.left
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.base.mvi_arch.BuildConfig
import com.base.mvi_arch.R
import com.base.mvi_arch.base.BaseActivity
import com.base.mvi_arch.data.Articles
import com.base.mvi_arch.databinding.ActivityMainBinding
import com.base.mvi_arch.network.KtorClient
import com.base.mvi_arch.network.fetchTestAPI
import com.base.mvi_arch.ui.main.action.MainViewAction
import com.base.mvi_arch.ui.main.factory.MainFragmentFactory
import com.base.mvi_arch.ui.main.state.MainViewState
import com.base.mvi_arch.ui.main.viewmodel.MainViewModel
import com.base.mvi_arch.utils.SerializableHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {


    companion object {
        const val FRAGMENT_COUNT = 2
    }

    override fun onBundle(bundle: Bundle) {
    }


    override fun init(savedInstanceState: Bundle?) {
        initWindow()
        initView()
        registerUIStateCallback()
        viewModel.dispatch(MainViewAction.GetCurrentTabIndex)
    }

    private fun initWindow() {
        WindowCompat.getInsetsController(window, findViewById(android.R.id.content)).apply {
            isAppearanceLightStatusBars = true
        }
    }


    private fun initView() {
        fetchPosts()
        binding?.viewpager?.also {
            it.isUserInputEnabled = false
            it.adapter = object : FragmentStateAdapter(this) {
                override fun getItemCount(): Int = FRAGMENT_COUNT

                override fun createFragment(position: Int): Fragment =
                    MainFragmentFactory.create(position)
            }
        }

        binding?.bottomNavigationView?.setActiveColor(R.color.selected_tint_color)
            ?.setInActiveColor(R.color.default_tint_color)
            ?.addItem(BottomNavigationItem(R.drawable.ic_travel, getString(R.string.travel)))
            ?.addItem(BottomNavigationItem(R.drawable.ic_video, getString(R.string.video)))
            ?.setTabSelectedListener(object : BottomNavigationBar.SimpleOnTabSelectedListener() {
                override fun onTabSelected(position: Int) {
                    binding?.viewpager?.currentItem = position
                    viewModel.dispatch(MainViewAction.SaveCurrentTabIndex(position))
                }
            })?.initialise()
    }

    private fun registerUIStateCallback() {
        lifecycleScope.launchWhenResumed {
            viewModel.state.collect { viewState ->
                when (viewState) {
                    is MainViewState.InitialDefaultTab -> {
                        binding?.bottomNavigationView?.selectTab(viewState.index)
                    }
                }
            }
        }
    }

    private fun fetchPosts() {
        lifecycleScope.launch {
            try {
                when(val result = fetchTestAPI()) {
                    is Either.Right -> {
                        val error = result.value
                        Log.e("Ktor", "fetchPosts:$error")
                    }

                    is Either.Left -> {
                        val articles = result.value
                        Log.i("Ktor", "fetchPosts:$articles")
                    }
                }

//                val posts = KtorClient.apiService.getArticles()
//                val list = SerializableHolder.toBeanOrNull<List<Articles>>(posts)

            } catch (e: Exception) {
                Log.e("Ktor", "fetchPosts:${e.message} ")
                e.printStackTrace()
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }


    override fun onDestroy() {
        super.onDestroy()
        MainFragmentFactory.clear()
    }

}