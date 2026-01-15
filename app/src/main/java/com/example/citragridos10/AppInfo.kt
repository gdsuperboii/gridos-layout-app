package com.example.citragridos10

import android.graphics.drawable.Drawable

/**
 * Data class representing an app or widget item
 */
sealed class LauncherItem {
    abstract val id: String
    abstract val label: String
}

/**
 * Represents an installed application
 */
data class AppInfo(
    override val id: String,
    override val label: String,
    val packageName: String,
    val icon: Drawable?,
    val activityName: String
) : LauncherItem() {
    companion object {
        const val TYPE_APP = 0
    }
}

/**
 * Represents a widget on the launcher
 */
data class WidgetInfo(
    override val id: String,
    override val label: String,
    val widgetId: Int,
    val spanX: Int = 1,
    val spanY: Int = 1,
    val positionX: Int = 0,
    val positionY: Int = 0
) : LauncherItem() {
    companion object {
        const val TYPE_WIDGET = 1
    }
}
