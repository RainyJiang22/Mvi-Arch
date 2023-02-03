package com.base.mvi_arch.ui.travel.adapter

import android.os.Bundle
import android.util.ArrayMap
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.base.mvi_arch.data.TravelResponse
import com.base.mvi_arch.global.gson
import com.base.mvi_arch.ui.travel.fragment.TravelTabFragment

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */
class TravelPageAdapter(fragment: Fragment, private val travelResponse: TravelResponse) :
    FragmentStateAdapter(fragment) {

    private val fragments by lazy { ArrayMap<String, Fragment?>(travelResponse.tabs.size) }

    override fun getItemCount(): Int {
        return travelResponse.tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        val tab = travelResponse.tabs[position]
        val bundle = Bundle()
        with(bundle) {
            putString(TravelTabFragment.URL, travelResponse.url)
            putString(TravelTabFragment.PARAMS, gson.toJson(travelResponse.params))
            putString(TravelTabFragment.GROUP_CHANNEL_CODE, tab.groupChannelCode)
            putInt(TravelTabFragment.TYPE, tab.type)
        }
        val fragment = TravelTabFragment.newInstance(bundle)
        fragments[tab.groupChannelCode] = fragment
        return fragment
    }
}