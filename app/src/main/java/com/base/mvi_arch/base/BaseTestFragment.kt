package com.base.mvi_arch.base

import androidx.fragment.app.Fragment

abstract class BaseTestFragment : Fragment() {

    private var mHasLoadedData = false

    override fun onResume() {
        super.onResume()
        if (!mHasLoadedData) {
            loadPageData()
            mHasLoadedData = true
        }
    }

    abstract fun loadPageData()

}