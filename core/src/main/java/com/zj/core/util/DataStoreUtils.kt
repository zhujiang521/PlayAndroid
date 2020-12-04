package com.zj.core.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.*
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

    suspend fun readBooleanData(key: String): Boolean {
        val readBoolean = readBooleanFlow(key)
        return readBoolean.first()
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

    suspend fun readIntData(key: String): Int {
        val readInt = readIntFlow(key)
        return readInt.first()
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

    suspend fun readStringData(key: String): String {
        val readString = readStringFlow(key)
        return readString.first()
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

    suspend fun readFloatData(key: String): Float {
        val readFloat = readFloatFlow(key)
        return readFloat.first()
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

    suspend fun readLongData(key: String): Long {
        val readLong = readLongFlow(key)
        return readLong.first()
    }

    suspend fun saveBooleanData(key: String, value: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    suspend fun saveIntData(key: String, value: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    suspend fun saveStringData(key: String, value: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    suspend fun saveFloatData(key: String, value: Float) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    suspend fun saveLongData(key: String, value: Long) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

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