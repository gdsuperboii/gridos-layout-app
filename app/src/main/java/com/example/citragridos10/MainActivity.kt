package com.example.citragridos10

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

/**
 * Main launcher activity that displays apps alphabetically sorted A-Z
 * with support for widgets and page-based navigation
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var pageAdapter: PageAdapter
    private lateinit var pageIndicator: LinearLayout
    private val itemsPerPage = 20 // Maximum items per page (4 columns x 5 rows)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBackground()
        setupDefaultIcons()
        setupViewPager()
        loadInstalledApps()
    }

    /**
     * Setup default icon pack
     * Default icons are automatically applied to common apps
     */
    private fun setupDefaultIcons() {
        // Default icons are now automatically enabled in IconPackManager
        // You can disable them by setting: iconPackManager.useDefaultIcons = false

        // Optional: Add custom icons for specific apps
        // Example:
        // iconPackManager.registerCustomIcon("com.example.app", R.drawable.custom_icon)
    }

    private fun setupBackground() {
        val backgroundView = findViewById<GifBackgroundView>(R.id.backgroundView)

        // TODO: Replace R.drawable.background_gif with your actual GIF file
        // Place your GIF file in res/drawable/ folder
        // For now, using a placeholder - uncomment the line below when you add your GIF
       backgroundView.setBackgroundGif(R.drawable.background_gif_2)

        // Temporary: Using a static image as placeholder
        // You can also use: backgroundView.setBackgroundFromPath("/path/to/your/gif.gif")
    }

    private fun setupViewPager() {
        viewPager = findViewById(R.id.viewPager)
        pageIndicator = findViewById(R.id.pageIndicator)

        // Create adapter with click handlers
        pageAdapter = PageAdapter(
            onAppClick = { appInfo ->
                launchApp(appInfo)
            },
            onAppLongClick = { appInfo ->
                showAppOptions(appInfo)
                true
            }
        )

        viewPager.adapter = pageAdapter

        // Setup page change listener for indicators
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updatePageIndicators(position)
            }
        })
    }

    /**
     * Load all installed apps and sort them alphabetically
     */
    private fun loadInstalledApps() {
        val packageManager = packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val apps = packageManager.queryIntentActivities(mainIntent, 0)
            .map { resolveInfo ->
                AppInfo(
                    id = resolveInfo.activityInfo.packageName,
                    label = resolveInfo.loadLabel(packageManager).toString(),
                    packageName = resolveInfo.activityInfo.packageName,
                    icon = resolveInfo.loadIcon(packageManager),
                    activityName = resolveInfo.activityInfo.name
                )
            }
            .sortedBy { it.label.lowercase() } // Sort alphabetically A-Z

        // Create pages
        val pages = mutableListOf<LauncherPage>()

        // First page with widgets
        val firstPageItems = mutableListOf<LauncherItem>()

        // Add Search Widget at the top (4x1 - full width)
        firstPageItems.add(
            WidgetInfo(
                id = "widget_search",
                label = "Search Widget",
                widgetId = 0,
                spanX = 4,
                spanY = 1
            )
        )

        // Add Clock Widget (2x1)
        firstPageItems.add(
            WidgetInfo(
                id = "widget_clock",
                label = "Clock Widget",
                widgetId = 1,
                spanX = 2,
                spanY = 1
            )
        )

        // Add Weather Widget (2x2)
        firstPageItems.add(
            WidgetInfo(
                id = "widget_weather",
                label = "Weather Widget",
                widgetId = 2,
                spanX = 2,
                spanY = 2
            )
        )

        // Add apps to first page (up to itemsPerPage - widgets count)
        val firstPageAppCount = (itemsPerPage - 7).coerceAtLeast(0) // Widgets take ~7 slots
        firstPageItems.addAll(apps.take(firstPageAppCount))
        pages.add(LauncherPage(0, firstPageItems))

        // Add remaining apps to additional pages
        var remainingApps = apps.drop(firstPageAppCount)
        var pageNumber = 1

        while (remainingApps.isNotEmpty()) {
            val pageItems = remainingApps.take(itemsPerPage)
            pages.add(LauncherPage(pageNumber, pageItems))
            remainingApps = remainingApps.drop(itemsPerPage)
            pageNumber++
        }

        // Submit pages to adapter
        pageAdapter.submitPages(pages)

        // Setup page indicators
        setupPageIndicators(pages.size)
        updatePageIndicators(0)
    }

    /**
     * Launch the selected app
     */
    private fun launchApp(appInfo: AppInfo) {
        try {
            val intent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
            if (intent != null) {
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Show options for the selected app (e.g., app info, uninstall)
     */
    private fun showAppOptions(appInfo: AppInfo) {
        // TODO: Implement app options dialog
        // This could show options like:
        // - App Info
        // - Uninstall
        // - Add to folder
        // etc.
    }

    /**
     * Setup page indicator dots
     */
    private fun setupPageIndicators(pageCount: Int) {
        pageIndicator.removeAllViews()

        val dotSize = (8 * resources.displayMetrics.density).toInt()
        val dotMargin = (8 * resources.displayMetrics.density).toInt()

        for (i in 0 until pageCount) {
            val dot = View(this)
            val params = LinearLayout.LayoutParams(dotSize, dotSize)
            params.setMargins(dotMargin, 0, dotMargin, 0)
            dot.layoutParams = params
            dot.setBackgroundResource(R.drawable.page_indicator_dot)
            pageIndicator.addView(dot)
        }
    }

    /**
     * Update page indicators to show current page
     */
    private fun updatePageIndicators(currentPage: Int) {
        for (i in 0 until pageIndicator.childCount) {
            val dot = pageIndicator.getChildAt(i)
            dot.alpha = if (i == currentPage) 1.0f else 0.3f
        }
    }
}
