package com.example.composeday3.data

import android.content.Context

class CounterRepository(context: Context) {

    private val prefs =
        context.getSharedPreferences("counter_prefs", Context.MODE_PRIVATE)

    fun saveCount(value: Int) {
        prefs.edit().putInt("count", value).apply()
    }

    fun loadCount(): Int {
        return prefs.getInt("count", 0)
    }

    fun saveStep(value: Int) {
        prefs.edit().putInt("step", value).apply()
    }

    fun loadStep(): Int {
        return prefs.getInt("step", 1)
    }
}
