package com.example.citragridos10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.citragridos10.widgets.ClockWidget
import com.example.citragridos10.widgets.SearchWidget
import com.example.citragridos10.widgets.WeatherWidget

/**
 * Adapter for displaying apps and widgets in the launcher
 * Apps are automatically sorted alphabetically A-Z
 */
class LauncherAdapter(
    private val onAppClick: (AppInfo) -> Unit,
    private val onAppLongClick: (AppInfo) -> Boolean = { false }
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<LauncherItem>()
    private var iconPackManager: IconPackManager? = null

    companion object {
        private const val VIEW_TYPE_APP = 0
        private const val VIEW_TYPE_WIDGET = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is AppInfo -> VIEW_TYPE_APP
            is WidgetInfo -> VIEW_TYPE_WIDGET
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Initialize icon pack manager if not already done
        if (iconPackManager == null) {
            iconPackManager = IconPackManager(parent.context)
        }

        return when (viewType) {
            VIEW_TYPE_APP -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_app, parent, false)
                AppViewHolder(view, iconPackManager)
            }
            VIEW_TYPE_WIDGET -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_widget, parent, false)
                WidgetViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is AppViewHolder -> {
                val appInfo = item as AppInfo
                holder.bind(appInfo, onAppClick, onAppLongClick)
            }
            is WidgetViewHolder -> {
                val widgetInfo = item as WidgetInfo
                holder.bind(widgetInfo)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    /**
     * Update the list of apps and widgets
     * Apps will be automatically sorted alphabetically
     */
    fun submitList(newItems: List<LauncherItem>) {
        items.clear()

        // Separate apps and widgets
        val apps = newItems.filterIsInstance<AppInfo>()
            .sortedBy { it.label.lowercase() } // Sort alphabetically A-Z
        val widgets = newItems.filterIsInstance<WidgetInfo>()

        // Add widgets first, then sorted apps
        items.addAll(widgets)
        items.addAll(apps)

        notifyDataSetChanged()
    }

    /**
     * Add a widget to the launcher
     */
    fun addWidget(widget: WidgetInfo) {
        items.add(widget)
        notifyItemInserted(items.size - 1)
    }

    /**
     * Remove a widget from the launcher
     */
    fun removeWidget(widgetId: Int) {
        val position = items.indexOfFirst {
            it is WidgetInfo && it.widgetId == widgetId
        }
        if (position != -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * ViewHolder for app items
     */
    class AppViewHolder(
        itemView: View,
        private val iconPackManager: IconPackManager?
    ) : RecyclerView.ViewHolder(itemView) {
        private val iconView: ImageView = itemView.findViewById(R.id.appIcon)
        private val iconBackgroundView: ImageView = itemView.findViewById(R.id.iconBackground)

        fun bind(
            appInfo: AppInfo,
            onAppClick: (AppInfo) -> Unit,
            onAppLongClick: (AppInfo) -> Boolean
        ) {
            // Apply custom icon styling
            val styledIcon = iconPackManager?.getStyledIcon(appInfo.icon, appInfo.packageName)
                ?: appInfo.icon

            iconView.setImageDrawable(styledIcon)

            // You can set custom background images per app here
            // Example: iconBackgroundView.setImageResource(R.drawable.custom_icon_bg)

            itemView.setOnClickListener { onAppClick(appInfo) }
            itemView.setOnLongClickListener { onAppLongClick(appInfo) }

            // Store the app info in the view tag for the layout manager
            itemView.tag = appInfo
        }
    }

    /**
     * ViewHolder for widget items
     */
    class WidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container: ViewGroup = itemView.findViewById(R.id.widgetContainer)

        fun bind(widgetInfo: WidgetInfo) {
            container.removeAllViews()

            // Create the appropriate custom widget based on the widget type
            val widget = when (widgetInfo.label) {
                "Clock Widget" -> ClockWidget(itemView.context)
                "Weather Widget" -> WeatherWidget(itemView.context)
                "Search Widget" -> SearchWidget(itemView.context)
                else -> null
            }

            widget?.let {
                container.addView(it, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ))
            }

            // Store the widget info in the view tag for the layout manager
            itemView.tag = widgetInfo
        }
    }
}
