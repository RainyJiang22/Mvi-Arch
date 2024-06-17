package com.simple.flowcache.stategy

import kotlinx.coroutines.flow.Flow
import com.simple.flowcache.RxCache
import com.simple.flowcache.model.CacheResult
import java.lang.reflect.Type

/**
 * Created by jaaksi on 2021/4/29..<br></br>
 *
 * 只加载缓存
 */
class OnlyCacheStrategy : BaseStrategy() {

    override fun <T> execute(
        cache: RxCache,
        cacheKey: String,
        netSource: Flow<CacheResult<T?>>,
        type: Type
    ): Flow<CacheResult<T?>> {
        return loadCache(cache, cacheKey, type)
    }
}