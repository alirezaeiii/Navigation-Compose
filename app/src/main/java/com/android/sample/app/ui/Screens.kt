package com.android.sample.app.ui

sealed class Screens(val title: String) {
    object Dashboard : Screens("dashboard_screen")
    object Section : Screens("section_screen/{$LINK}")

    companion object {
        const val LINK = "link"
    }
}