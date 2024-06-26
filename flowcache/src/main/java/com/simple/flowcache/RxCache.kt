package com.simple.flowcache

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import com.simple.flowcache.converter.GsonCacheConverter
import com.simple.flowcache.core.CacheCore
import com.simple.flowcache.core.LruDiskCache
import com.simple.flowcache.exception.NoCacheException
import com.simple.flowcache.model.RealEntity
import com.simple.flowcache.type.CacheType
import com.simple.flowcache.util.CacheLog
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.reflect.Type

/**
 * 缓存类，支持普通操作及rx操作<br>
 * 支持设置缓存磁盘大小、缓存key、缓存时间、缓存存储的转换器、缓存目录、缓存Version<br>
 */
object RxCache {

    const val TAG = "RxCache"
    const val NEVER_EXPIRE = -1L //缓存过期时间，默认永久缓存
    private const val MAX_CACHE_SIZE = 50 * 1024 * 1024L // 50MB

    //缓存的核心管理类
    private lateinit var cacheCore: CacheCore

    /**
     * 初始化，默认目录 externalCacheDir/rxcache
     */
    fun initialize(context: Context) {
        initialize(File(context.cacheDir.path, "rxcache"))
    }

    /**
     * 初始化
     *
     * @param cacheDir       缓存目录
     * @param cacheVersion   缓存版本
     * @param maxCacheSize   缓存最大size
     * @param cacheConverter 缓存Converter
     */
    fun initialize(
        cacheDir: File,
        cacheConverter: GsonCacheConverter = GsonCacheConverter(Gson()),
        cacheVersion: Int = 1,
        maxCacheSize: Long = MAX_CACHE_SIZE,

        ) {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        cacheCore =
            CacheCore(LruDiskCache(cacheConverter, cacheDir, cacheVersion, maxCacheSize))
    }

    fun containsKey(key: String): Boolean {
        return cacheCore.containsKey(key)
    }

    /**
     * 同步获取缓存
     */
    fun <T> get(key: String, clazz: Class<T>): T? {
        return get(key, clazz as Type)
    }

    /**
     * 同步获取缓存
     */
    fun <T> get(key: String, cacheType: CacheType<T>): T? {
        return get(key, cacheType.type)
    }

    /**
     * get中的
     * 同步获取缓存
     */
    fun <T> get(key: String, type: Type): T? {
        return cacheCore.load<T>(type, key)?.data
    }

    fun <T> rxGet(key: String, clazz: Class<T>): Flow<T?> {
        return rxGet(key, clazz as Type)
    }

    fun <T> rxGet(key: String, cacheType: CacheType<T>): Flow<T?> {
        return rxGet(key, cacheType.type)
    }

    /**
     * @param type 保存的类型
     * @param key  缓存key
     */
    fun <T> rxGet(key: String, type: Type): Flow<T?> {
        return flow<T> {
            val value = get<T>(key,type)
            if (value != null) {
                emit(value)
            } else {
                throw NoCacheException()
            }
        }.flowOn(Dispatchers.IO)
    }

    internal fun <T> rxGetInner(key: String, type: Type): Flow<T> {
        return flow<T> {
            val value = get<T>(key, type)
            if (value != null) {
                emit(value)
            } else {
                throw NoCacheException()
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 同步保存
     *
     * @param duration 毫秒ms
     */
    fun <T> put(key: String, value: T, duration: Long = NEVER_EXPIRE): Boolean {
        check(duration == NEVER_EXPIRE || duration > 0)
        return cacheCore.save(key, RealEntity(value, duration))
    }

    /**
     * 通过Rx的方式保存，返回一个Flow
     *
     * @param duration ms
     */
    fun <T> rxPut(key: String, value: T, duration: Long = NEVER_EXPIRE): Flow<Boolean> {
        check(duration == NEVER_EXPIRE || duration > 0)
        return { cacheCore.save(key, RealEntity(value, duration)) }.asFlow()
            .catch { CacheLog.e(TAG, it) }
            .flowOn(Dispatchers.IO)
    }

    /**
     * 写一个通用缓存方法
     */
    fun <T> rxCache(key: String, value: T, duration: Long = NEVER_EXPIRE): Flow<T> {
        check(duration == NEVER_EXPIRE || duration > 0)
        return flow<T> {
            val entity = RealEntity(value, duration)
            val result = withContext(Dispatchers.IO) {
                cacheCore.save(key, entity)
            }
            if (result) {
                emit(value)
            } else {
                throw Exception("Failed to save data")
            }
        }.catch { e ->
            throw e
        }.flowOn(Dispatchers.IO)
    }


    /**
     * 同步删除缓存
     */
    fun remove(key: String): Boolean {
        return cacheCore.remove(key)
    }

    /**
     * rx remove
     */
    fun rxRemove(key: String): Flow<Boolean> {
        return { cacheCore.remove(key) }.asFlow()
            .catch { CacheLog.e(TAG, it) }
            .flowOn(Dispatchers.IO)
    }

    /**
     * 异步移除缓存（key）
     */
    suspend fun removeAsync(key: String) {
        rxRemove(key).collect()
    }

    /**
     * 清空缓存
     */
    fun clear(): Boolean {
        return cacheCore.clear()
    }

    fun rxClear(): Flow<Boolean> {
        return { cacheCore.clear() }.asFlow()
            .catch { CacheLog.e(TAG, it) }
            .flowOn(Dispatchers.IO)
    }

    /**
     * 异步清空缓存
     */
    suspend fun clearAsync() {
        rxClear().collect()
    }

}