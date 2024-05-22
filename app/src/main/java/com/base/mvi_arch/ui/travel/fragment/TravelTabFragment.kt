package com.base.mvi_arch.ui.travel.fragment

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.mvi_arch.R
import com.base.mvi_arch.base.BaseFragment
import com.base.mvi_arch.base.EmptyViewModel
import com.base.mvi_arch.data.Params
import com.base.mvi_arch.databinding.FragmentTravelListBinding
import com.base.mvi_arch.event.GlobalEvent
import com.base.mvi_arch.global.fromJson
import com.base.mvi_arch.startActivity
import com.base.mvi_arch.ui.travel.action.TravelTabViewAction
import com.base.mvi_arch.ui.travel.activity.TravelDetailActivity
import com.base.mvi_arch.ui.travel.activity.TravelDetailActivity.Companion.KEY_TITLE
import com.base.mvi_arch.ui.travel.activity.TravelDetailActivity.Companion.KEY_URL
import com.base.mvi_arch.ui.travel.adapter.TravelTabAdapter
import com.base.mvi_arch.ui.travel.state.TravelTabViewState
import com.base.mvi_arch.ui.travel.viewmodel.TravelTabViewModel
import com.biubiu.eventbus.observe.observeEvent
import com.biubiu.eventbus.post.postEvent
import com.biubiu.eventbus.store.ApplicationScopeViewModelProvider
import com.biubiu.eventbus.util.removeStickyEvent
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * @author jiangshiyu
 * @date 2023/2/3
 */
class TravelTabFragment : BaseFragment<FragmentTravelListBinding, TravelTabViewModel>(),
    OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


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


    private lateinit var mRequestParams: Params
    private val mAdapter by lazy { TravelTabAdapter(Glide.with(this)) }
    private var mRequestUrl = ""
    private var mGroupChannelCode = ""
    private var mCurrentPage = 0

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        initView()
        initRequestParams()
        registerUIStateCallback()
    }

    override fun loadPageData() {
        mRequestParams.pagePara.pageIndex = mCurrentPage
        viewModel.dispatch(TravelTabViewAction.Refresh(mRequestUrl, mRequestParams))
    }

    private fun initRequestParams() {
        arguments?.let { bundle ->
            mRequestUrl = bundle.getString(URL).toString()
            mRequestParams = fromJson(bundle.getString(PARAMS).toString())
            mGroupChannelCode = bundle.getString(GROUP_CHANNEL_CODE).toString()
            mRequestParams.groupChannelCode = bundle.getString(GROUP_CHANNEL_CODE).toString()
            mRequestParams.type = bundle.getInt(TYPE).toString()
        }
    }

    private fun registerUIStateCallback() {
        lifecycleScope.launchWhenResumed {
            viewModel.state.flowWithLifecycle(lifecycle).distinctUntilChanged()
                .collect { viewState ->
                    when (viewState) {
                        is TravelTabViewState.LoadingState -> {
                            binding?.swipeRefreshLayout?.isRefreshing = true
                        }

                        is TravelTabViewState.RefreshSuccess -> {
                            binding?.swipeRefreshLayout?.isRefreshing = false
                            mAdapter.setList(viewState.travelList)
                        }

                        is TravelTabViewState.LoadMoreSuccess -> {
                            binding?.swipeRefreshLayout?.isRefreshing = false
                            mAdapter.addData(viewState.travelList)
                            if (viewState.travelList.isEmpty()) {
                                mAdapter.loadMoreModule.loadMoreEnd()
                            } else {
                                mAdapter.loadMoreModule.loadMoreComplete()
                            }
                        }

                        is TravelTabViewState.LoadError -> {
                            binding?.swipeRefreshLayout?.isRefreshing = false
                            Toast.makeText(requireContext(), viewState.errorMsg, Toast.LENGTH_LONG)
                                .show()
                            mAdapter.loadMoreModule.loadMoreFail()
                        }
                    }
                }
        }
    }


    private fun initView() {
        mCurrentPage = 0
        binding?.swipeRefreshLayout?.setColorSchemeResources(R.color.selected_tint_color)
        binding?.swipeRefreshLayout?.setOnRefreshListener(this)
        binding?.rcvVideo?.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mAdapter.loadMoreModule.setOnLoadMoreListener(this)
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val title = mAdapter.data[position].article.articleTitle
            val h5Url = mAdapter.data[position].article.urls.find {
                it.h5Url != null
            }?.h5Url
            h5Url?.let {
                //旅游详情界面
                startActivity<TravelDetailActivity>(Bundle().apply {
                    putString(KEY_TITLE, title)
                    putString(KEY_URL, it)
                })
            }
        }
        binding?.rcvVideo?.adapter = mAdapter
    }

    override fun onLoadMore() {
        ++mCurrentPage
        mRequestParams.pagePara.pageIndex = mCurrentPage
        viewModel.dispatch(TravelTabViewAction.LoadMore(mRequestUrl, mRequestParams))
    }

    override fun onRefresh() {
        mCurrentPage = 0
        mRequestParams.pagePara.pageIndex = mCurrentPage
        viewModel.dispatch(TravelTabViewAction.LoadMore(mRequestUrl, mRequestParams))
    }

    override fun onResume() {
        super.onResume()
        observeEvent<GlobalEvent> {
            Log.i("Event", "received GlobalEvent ${it.name}")
        }
    }


}