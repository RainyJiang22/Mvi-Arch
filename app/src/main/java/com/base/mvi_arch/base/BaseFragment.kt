package com.base.mvi_arch.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<V : ViewBinding, VM : AndroidViewModel> : Fragment() {

    var binding: V? = null

    val viewModel: VM by lazy {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val clazz = types[1] as Class<VM>
        ViewModelProvider(
            requireActivity().viewModelStore,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(clazz)
    }

    private var mHasLoadedData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            onBundle(requireArguments())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return setContentView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }


    open fun onVisible() {


    }


    open fun onHidden() {

    }

    override fun onResume() {
        super.onResume()
        if (isAdded && !isHidden) {
            onVisible()
        }
        if (!mHasLoadedData) {
            loadPageData()
            mHasLoadedData = true
        }
    }

    override fun onPause() {
        if (isVisible) {
            onHidden()
        }
        super.onPause()
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onVisible()
        } else {
            onHidden()
        }
    }


    private fun setContentView(inflater: LayoutInflater, container: ViewGroup?): View? {
        if (view == null) {
            val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
            val aClass = types[0] as Class<V>
            try {
                binding =
                    aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
                        .invoke(null, getLayoutInflater()) as V?
                return binding?.root
            } catch (e: Error) {
                e.printStackTrace();
            }
        }
        return null
    }


    abstract fun onBundle(bundle: Bundle)

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun loadPageData()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    fun binding(block: V?.() -> Unit) {
        block.invoke(binding)
    }


}