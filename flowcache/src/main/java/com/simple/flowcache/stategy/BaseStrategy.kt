package com.simple.flowcache.stategy

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.simple.flowcache.RxCache
import com.simple.flowcache.model.CacheResult
import java.lang.reflect.Type

/**
 *
 * 缓存策略的base抽象类，提供 load data from cache and remote
 */
abstract class BaseStrategy : IStrategy {

    fun <T> loadCache(
        cache: RxCache, cacheKey: String, type: Type
    ): Flow<CacheResult<T?>> {
        return cache.rxGetInner<T?>(cacheKey, type).map {
            CacheResult(true, it)
        }
    }
}