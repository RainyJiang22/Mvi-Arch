package com.base.mvi_arch.ui.video.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.mvi_arch.R
import com.base.mvi_arch.base.BaseFragment
import com.base.mvi_arch.base.EmptyViewModel
import com.base.mvi_arch.databinding.FragmentVideoBinding
import com.base.mvi_arch.toast
import com.base.mvi_arch.ui.video.action.VideoListViewAction
import com.base.mvi_arch.ui.video.adapter.VideoListAdapter
import com.base.mvi_arch.ui.video.state.VideoListViewState
import com.base.mvi_arch.ui.video.viewmodel.VideoViewModel
import com.base.mvi_arch.utils.ShareUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.zy.multistatepage.state.EmptyState
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class VideoFragment : BaseFragment<FragmentVideoBinding, VideoViewModel>(),
    SwipeRefreshLayout.OnRefreshListener,
    OnLoadMoreListener,
    OnItemChildClickListener {


    private val mAdapter by lazy { VideoListAdapter(Glide.with(this)) }
    private var mCurrentPage = 0

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        initView()
        registerUIStateCallback()
    }


    private fun registerUIStateCallback() {
        lifecycleScope.launchWhenResumed {
            viewModel.state.flowWithLifecycle(lifecycle).distinctUntilChanged().collect { state ->
                binding?.swipeRefreshLayout?.isRefreshing = false
                when (state) {
                    is VideoListViewState.LoadingState -> binding?.multiStateContainer?.show<LoadingState>()
                    is VideoListViewState.RefreshSuccessState -> {
                        mAdapter.setList(state.videoList)
                        if (state.videoList.isEmpty())
                            binding?.multiStateContainer?.show<EmptyState>()
                        else
                            binding?.multiStateContainer?.show<SuccessState>()
                    }
                    is VideoListViewState.LoadMoreSuccessState -> {
                        mAdapter.addData(state.videoList)
                        if (state.videoList.isEmpty()) {
                            mAdapter.loadMoreModule.loadMoreEnd()
                        } else {
                            mAdapter.loadMoreModule.loadMoreComplete()
                        }
                    }
                    is VideoListViewState.LoadErrorState -> {
                        if (mAdapter.itemCount == 0) {
                            binding?.multiStateContainer?.show<ErrorState> {
                                it.retry {
                                    retry()
                                }
                            }
                        }
                        onLoadError(state.errorMsg)
                    }
                    is VideoListViewState.LoadMoreErrorState -> {
                        onLoadError(state.errorMsg)
                    }
                }
            }
        }
    }

    private fun retry() {
        mCurrentPage = 0
        viewModel.dispatch(VideoListViewAction.Retry)
    }


    private fun initView() {
        mCurrentPage = 0
        binding?.swipeRefreshLayout?.setColorSchemeResources(R.color.selected_tint_color)
        binding?.swipeRefreshLayout?.setOnRefreshListener(this)
        binding?.rcvVideo?.layoutManager = LinearLayoutManager(requireContext())
        mAdapter.addChildClickViewIds(R.id.iv_share)
        mAdapter.setOnItemChildClickListener(this)
        mAdapter.loadMoreModule.setOnLoadMoreListener(this)
        binding?.rcvVideo?.adapter = mAdapter
    }


    private fun onLoadError(errorMsg: String) {
        requireActivity().toast(errorMsg, Toast.LENGTH_LONG)
        mAdapter.loadMoreModule.loadMoreFail()
    }

    override fun loadPageData() {
        viewModel.dispatch(VideoListViewAction.Refresh)
    }

    override fun onRefresh() {
        mCurrentPage = 0
        viewModel.dispatch(VideoListViewAction.Refresh)
    }

    override fun onLoadMore() {
        viewModel.dispatch(VideoListViewAction.LoadMore(++mCurrentPage))
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (view.id == R.id.iv_share) {
            ShareUtils.share(
                requireContext(),
                mAdapter.data[position].title,
                mAdapter.data[position].playUrl
            )
        }

    }
}