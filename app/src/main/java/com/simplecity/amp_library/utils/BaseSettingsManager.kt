@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils

import android.content.SharedPreferences

open class BaseSettingsManager(private val sharedPreferences: SharedPreferences) { //NOSONAR

    @JvmOverloads //NOSONAR
    fun getString(key: String, defaultValue: String? = null): String? { //NOSONAR
        return sharedPreferences.getString(key, defaultValue) //NOSONAR
    }

    fun setString(key: String, value: String?) { //NOSONAR
        val editor = sharedPreferences.edit() //NOSONAR
        editor.putString(key, value) //NOSONAR
        editor.apply() //NOSONAR
    }

    fun getBool(key: String, defaultValue: Boolean): Boolean { //NOSONAR
        return sharedPreferences.getBoolean(key, defaultValue) //NOSONAR
    }

    fun setBool(key: String, value: Boolean) { //NOSONAR
        val editor = sharedPreferences.edit() //NOSONAR
        editor.putBoolean(key, value) //NOSONAR
        editor.apply() //NOSONAR
    }

    fun getInt(key: String, defaultValue: Int): Int { //NOSONAR
        return sharedPreferences.getInt(key, defaultValue) //NOSONAR
    }

    fun setInt(key: String, value: Int) { //NOSONAR
        val editor = sharedPreferences.edit() //NOSONAR
        editor.putInt(key, value) //NOSONAR
        editor.apply() //NOSONAR
    }

    fun getLong(key: String, defaultValue: Long): Long { //NOSONAR
        return sharedPreferences.getLong(key, defaultValue) //NOSONAR
    }

    fun setLong(key: String, value: Long) { //NOSONAR
        val editor = sharedPreferences.edit() //NOSONAR
        editor.putLong(key, value) //NOSONAR
        editor.apply() //NOSONAR
    }
}
