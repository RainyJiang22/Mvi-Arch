package com.base.mvi_arch.utils

import android.content.Context
import android.content.Intent

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
object ShareUtils {

    private const val TEXT_PLAIN = "text/plain"

    fun share(context: Context, title: String, content: String) {
        with(Intent()) {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, content)
            type = TEXT_PLAIN
            context.startActivity(Intent.createChooser(this, title))
        }
    }
}