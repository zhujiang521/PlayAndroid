package com.zj.core.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * SharedPreferences operation, you need application to invoke setContext
 * @param name sp key name
 * @param default default value
 */

const val SHARED_NAME = "_preferences"
class Preference<T>(private val name: String, private val default: T) : ReadWriteProperty<Any?, T> {



    companion object {
        lateinit var preferences: SharedPreferences
        /**
         * init Context
         * @param context Context
         */
        fun setContext(context: Context) {
            preferences = context.getSharedPreferences(
                context.packageName + SHARED_NAME,
                Context.MODE_PRIVATE
            )
        }

        fun clear() {
            preferences.edit().clear().apply()
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = findPreference(name, default)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = putPreference(name, value)

    @Suppress("UNCHECKED_CAST")
    private fun <U> findPreference(name: String, default: U): U = with(preferences) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }!!
        res as U
    }

    @SuppressLint("CommitPrefEdits")
    private fun <U> putPreference(name: String, value: U) = with(preferences.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }.apply()
    }
}