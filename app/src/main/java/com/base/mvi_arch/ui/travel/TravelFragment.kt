package com.base.mvi_arch.ui.travel

import android.os.Bundle
import com.base.mvi_arch.base.BaseFragment
import com.base.mvi_arch.base.EmptyViewModel
import com.base.mvi_arch.databinding.FragmentTravelBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */

@AndroidEntryPoint
class TravelFragment : BaseFragment<FragmentTravelBinding, EmptyViewModel>() {
    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {

    }
}