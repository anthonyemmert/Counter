package com.example.composeday3.data

import android.content.Context

class SettingsRepository(context: Context) {

    private val prefs =
        context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    fun saveDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("dark_mode", enabled).apply()
    }

    fun loadDarkMode(): Boolean {
        return prefs.getBoolean("dark_mode", false)
    }
}