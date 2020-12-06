package com.zj.core.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：12/3/20
 * 描述：DataStore 工具类
 *
 */
class DataStoreUtils private constructor(ctx: Context) {

    private val preferenceName = "PlayAndroidDataStore"
    private var context: Context = ctx
    private var dataStore: DataStore<Preferences>

    init {
        dataStore = context.createDataStore(preferenceName)
    }

    fun readBooleanFlow(key: String): Flow<Boolean> =
        dataStore.data
            .catch {
                //当读取数据遇到错误时，如果是 `IOException` 异常，发送一个 emptyPreferences 来重新使用
                //但是如果是其他的异常，最好将它抛出去，不要隐藏问题
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferencesKey(key)] ?: false
            }

    fun readBooleanData(key: String): Boolean {
        var value = false
        runBlocking {
            dataStore.data.first {
                value = it[preferencesKey(key)] ?: false
                true
            }
        }
        return value
    }

    fun readIntFlow(key: String): Flow<Int> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferencesKey(key)] ?: 0
            }

    fun readIntData(key: String): Int {
        var value = 0
        runBlocking {
            dataStore.data.first {
                value = it[preferencesKey(key)] ?: 0
                true
            }
        }
        return value
    }

    fun readStringFlow(key: String): Flow<String> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferencesKey(key)] ?: ""
            }

    fun readStringData(key: String): String {
        var value = ""
        runBlocking {
            dataStore.data.first {
                value = it[preferencesKey(key)] ?: ""
                true
            }
        }
        return value
    }

    fun readFloatFlow(key: String): Flow<Float> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferencesKey(key)] ?: 0f
            }

    fun readFloatData(key: String): Float {
        var value = 0f
        runBlocking {
            dataStore.data.first {
                value = it[preferencesKey(key)] ?: 0f
                true
            }
        }
        return value
    }

    fun readLongFlow(key: String): Flow<Long> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferencesKey(key)] ?: 0L
            }

    fun readLongData(key: String): Long {
        var value = 0L
        runBlocking {
            dataStore.data.first {
                value = it[preferencesKey(key)] ?: 0L
                true
            }
        }
        return value
    }

    suspend fun saveBooleanData(key: String, value: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    fun saveSyncBooleanData(key: String, value: Boolean) =
        runBlocking { saveBooleanData(key, value) }

    suspend fun saveIntData(key: String, value: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    fun saveSyncIntData(key: String, value: Int) = runBlocking { saveIntData(key, value) }

    suspend fun saveStringData(key: String, value: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    fun saveSyncStringData(key: String, value: String) = runBlocking { saveStringData(key, value) }

    suspend fun saveFloatData(key: String, value: Float) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    fun saveSyncFloatData(key: String, value: Float) = runBlocking { saveFloatData(key, value) }

    suspend fun saveLongData(key: String, value: Long) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    fun saveSyncLongData(key: String, value: Long) = runBlocking { saveLongData(key, value) }

    companion object {
        @Volatile
        private var instance: DataStoreUtils? = null

        fun getInstance(ctx: Context): DataStoreUtils {
            if (instance == null) {
                synchronized(DataStoreUtils::class) {
                    if (instance == null) {
                        instance = DataStoreUtils(ctx)
                    }
                }
            }
            return instance!!
        }
    }

}