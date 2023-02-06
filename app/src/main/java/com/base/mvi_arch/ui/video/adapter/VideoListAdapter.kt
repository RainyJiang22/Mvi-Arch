package com.base.mvi_arch.ui.video.adapter

import android.content.Context
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import coil.load
import com.base.mvi_arch.R
import com.base.mvi_arch.data.VideoModel
import com.base.mvi_arch.global.ConfigKeys
import com.base.mvi_arch.global.Configurator
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.just.agentweb.AgentWebUtils

/**
 * @author jiangshiyu
 * @date 2023/2/6
 */
class VideoListAdapter(val glide: RequestManager) :
    BaseQuickAdapter<VideoModel, BaseViewHolder>(R.layout.item_video), LoadMoreModule {

    private val mOutlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(
                0, 0, view.width, view.height, AgentWebUtils.dp2px(
                    Configurator.getConfiguration<Context>(
                        ConfigKeys.APPLICATION_CONTEXT
                    ), 4f
                ).toFloat()
            )
        }
    }

    override fun convert(holder: BaseViewHolder, item: VideoModel) {

        //预览图
        glide.load(item.userPic)
            .into(holder.getView(R.id.iv_author))

        holder.getView<JzvdStd>(R.id.video_player).apply {
            setUp(item.playUrl, item.title, Jzvd.SCREEN_NORMAL)
            posterImageView.load(item.coverUrl)
            outlineProvider = mOutlineProvider
            clipToOutline = true
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        val jzvd: Jzvd?= holder.getViewOrNull(R.id.video_player)

        if (jzvd != null && Jzvd.CURRENT_JZVD != null && jzvd.jzDataSource
                .containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.currentUrl)
        ) {
            if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                Jzvd.releaseAllVideos()
            }
        }
    }


}