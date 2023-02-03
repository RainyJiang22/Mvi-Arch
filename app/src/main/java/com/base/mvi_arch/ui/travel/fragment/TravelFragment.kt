package com.base.mvi_arch.ui.travel.fragment

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.base.mvi_arch.BuildConfig
import com.base.mvi_arch.base.BaseFragment
import com.base.mvi_arch.base.EmptyViewModel
import com.base.mvi_arch.data.TravelResponse
import com.base.mvi_arch.data.TravelTabResponse
import com.base.mvi_arch.databinding.FragmentTravelBinding
import com.base.mvi_arch.toast
import com.base.mvi_arch.ui.travel.action.TravelViewAction
import com.base.mvi_arch.ui.travel.adapter.TravelPageAdapter
import com.base.mvi_arch.ui.travel.state.TravelViewState
import com.base.mvi_arch.ui.travel.viewmodel.TravelViewModel
import com.drake.statelayout.state
import com.google.android.material.tabs.TabLayoutMediator
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.tan

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */

class TravelFragment : BaseFragment<FragmentTravelBinding, TravelViewModel>() {
    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        refreshUIStateCallback()
    }


    private fun refreshUIStateCallback() {
        lifecycleScope.launchWhenResumed {
            viewModel.state.flowWithLifecycle(lifecycle).distinctUntilChanged().collect { state ->
                when (state) {
                    is TravelViewState.LoadingState -> binding?.multiStateContainer?.show<LoadingState>()
                    is TravelViewState.LoadSuccess -> {
                        binding?.multiStateContainer?.show<SuccessState>()
                        initTravelTab(state.travelViewResponse)
                    }
                    is TravelViewState.LoadFail -> {
                        Log.e("Travel", "refreshUIStateCallback: ${state.errorMsg}")
                        binding?.multiStateContainer?.show<ErrorState> {
                            it.retry {
                                viewModel.dispatch(TravelViewAction.Retry)
                            }
                        }
                        requireContext().toast(state.errorMsg)
                    }
                }
            }
        }
    }

    private fun initTravelTab(travelResponse: TravelResponse) {
        for (tab in travelResponse.tabs) {
            binding?.tabLayout?.let {
                it.addTab(it.newTab())
            }
            binding?.viewpager?.apply {
                adapter = TravelPageAdapter(this@TravelFragment, travelResponse)
                offscreenPageLimit = travelResponse.tabs.size
                TabLayoutMediator(binding!!.tabLayout, binding!!.viewpager) { tab, position ->
                    tab.text = travelResponse.tabs[position].labelName
                }.attach()
            }
        }
    }

    override fun loadPageData() {
        viewModel.dispatch(TravelViewAction.GetTravelTabs)
    }

}