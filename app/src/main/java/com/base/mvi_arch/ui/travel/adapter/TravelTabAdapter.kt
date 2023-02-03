package com.base.mvi_arch.ui.travel.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import cn.jzvd.JZUtils
import com.base.mvi_arch.R
import com.base.mvi_arch.data.TravelItem
import com.base.mvi_arch.global.ConfigKeys
import com.base.mvi_arch.global.Configurator
import com.bumptech.glide.RequestManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.just.agentweb.AgentWebUtils


/**
 * @author jiangshiyu
 * @date 2023/2/3
 */
class TravelTabAdapter(val glide: RequestManager) :
    BaseQuickAdapter<TravelItem, BaseViewHolder>(R.layout.item_travel),
    LoadMoreModule {

    private val mMaxImageWidth =
        (JZUtils.getScreenWidth(Configurator.getConfiguration<Context>(ConfigKeys.APPLICATION_CONTEXT))
                - AgentWebUtils.dp2px(
            Configurator.getConfiguration<Context>(ConfigKeys.APPLICATION_CONTEXT),
            20f
        )) / 2

    override fun convert(holder: BaseViewHolder, item: TravelItem) {
        val coverImageInfo = item.article.images[0]
        val layoutParams = holder.getView<ImageView>(R.id.iv_cover).layoutParams
        layoutParams.width = mMaxImageWidth
        layoutParams.height =
            (mMaxImageWidth * coverImageInfo.height / coverImageInfo.width).toInt()
        glide.load(item.article.images[0].dynamicUrl)
            .into(holder.getView(R.id.iv_cover))

        holder.getView<TextView>(R.id.tv_location).text = item.article.pois[0].poiName
        holder.getView<TextView>(R.id.tv_title).text = item.article.articleTitle

        glide.load(item.article.author.coverImage.dynamicUrl)
            .circleCrop()
            .into(holder.getView(R.id.iv_author))

        holder.getView<TextView>(R.id.tv_author).text = item.article.author.nickName

        holder.getView<TextView>(R.id.tv_like_count).text = item.article.likeCount.toString()

    }

}