package com.base.mvi_arch.ui.main.factory

import android.util.SparseArray
import androidx.fragment.app.Fragment
import com.base.mvi_arch.ui.travel.TravelFragment
import com.base.mvi_arch.ui.video.VideoFragment

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class MainFragmentFactory {

    companion object {

        private val fragments by lazy { SparseArray<Fragment?>() }

        @JvmStatic
        fun create(position: Int): Fragment {
            var fragment = fragments.get(position)
            if (fragment == null) {
                fragment = when (position) {
                    0 -> TravelFragment()
                    else -> VideoFragment()
                }
                fragments.put(position, fragment)
            }
            return fragment
        }

        @JvmStatic
        fun clear() {
            fragments.clear()
        }
    }
}