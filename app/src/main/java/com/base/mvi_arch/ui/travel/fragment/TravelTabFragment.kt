package com.base.mvi_arch.ui.travel.fragment

import android.os.Bundle
import com.base.mvi_arch.base.BaseFragment
import com.base.mvi_arch.base.EmptyViewModel
import com.base.mvi_arch.databinding.FragmentTravelListBinding

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */
class TravelTabFragment : BaseFragment<FragmentTravelListBinding, EmptyViewModel>() {


    companion object {
        const val URL = "url"
        const val PARAMS = "params"
        const val GROUP_CHANNEL_CODE = "groupChannelCode"
        const val TYPE = "type"
        fun newInstance(arguments: Bundle): TravelTabFragment {
            val travelTabFragment = TravelTabFragment()
            travelTabFragment.arguments = arguments
            return travelTabFragment
        }
    }

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun loadPageData() {
    }


}