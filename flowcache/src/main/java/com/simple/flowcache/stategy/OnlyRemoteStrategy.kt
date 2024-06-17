package com.simple.flowcache.stategy

import kotlinx.coroutines.flow.Flow
import com.simple.flowcache.RxCache
import com.simple.flowcache.model.CacheResult
import java.lang.reflect.Type

/**
 * 只请求网络，但数据依然会被缓存
 */
open class OnlyRemoteStrategy : IStrategy {
    override fun <T> execute(
        cache: RxCache,
        cacheKey: String,
        netSource: Flow<CacheResult<T?>>,
        type: Type
    ): Flow<CacheResult<T?>> {
        return netSource
    }
}