package com.example.composeday3




object Routes {
    const val COUNTER = "counter"
    const val ABOUT = "about"
    const val SETTINGS = "settings"
}

fun routeTitle(route: String?): String {
    return when (route) {
        Routes.COUNTER -> "Counter"
        Routes.ABOUT -> "About"
        else -> "App"
    }
}