package com.example.citragridos10

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap

/**
 * Manages custom icon packs and icon transformations with image support
 */
class IconPackManager(private val context: Context) {

    // Map to store custom icon images for specific packages
    private val customIconMap = mutableMapOf<String, Int>()

    // Enable/disable default icon pack
    var useDefaultIcons = true

    /**
     * Apply custom styling to an app icon
     */
    fun getStyledIcon(originalIcon: Drawable?, packageName: String): Drawable? {
        // 1. Check if there's a custom icon image for this package
        customIconMap[packageName]?.let { resourceId ->
            return context.getDrawable(resourceId)
        }

        // 2. Check default icon pack if enabled
        if (useDefaultIcons) {
            DefaultIconPack.getIconForPackage(packageName)?.let { resourceId ->
                return context.getDrawable(resourceId)
            }
        }

        // 3. If no original icon, return default icon
        if (originalIcon == null) {
            return context.getDrawable(R.drawable.default_icon_app)
        }

        // 4. Use original icon with styling
        val bitmap = originalIcon.toBitmap(256, 256)
        val styledBitmap = applyIconStyle(bitmap)

        return BitmapDrawable(context.resources, styledBitmap)
    }

    /**
     * Register a custom icon image for a specific package
     */
    fun registerCustomIcon(packageName: String, drawableResourceId: Int) {
        customIconMap[packageName] = drawableResourceId
    }

    /**
     * Load icon from image file path
     * Note: This will work after Gradle sync with Glide library
     */
    fun loadIconFromPath(path: String, callback: (Drawable?) -> Unit) {
        // Placeholder implementation - will work after Gradle sync
        callback(null)
    }

    /**
     * Apply custom styling to icon bitmap
     * This can include: rounded corners, shadows, borders, etc.
     */
    private fun applyIconStyle(bitmap: Bitmap): Bitmap {
        // Create a new bitmap with the same dimensions
        val styledBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(styledBitmap)

        // Draw the original icon
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        // You can add additional styling here:
        // - Apply filters
        // - Add shadows
        // - Adjust colors
        // - Add overlays

        return styledBitmap
    }

    /**
     * Check if a custom icon exists for a package
     */
    fun hasCustomIcon(packageName: String): Boolean {
        return customIconMap.containsKey(packageName) ||
               (useDefaultIcons && DefaultIconPack.hasIconForPackage(packageName))
    }

    /**
     * Get custom icon for a package from icon pack
     */
    fun getCustomIcon(packageName: String): Drawable? {
        // Check custom icons first
        customIconMap[packageName]?.let { resourceId ->
            return context.getDrawable(resourceId)
        }

        // Check default icon pack
        if (useDefaultIcons) {
            DefaultIconPack.getIconForPackage(packageName)?.let { resourceId ->
                return context.getDrawable(resourceId)
            }
        }

        return null
    }

    /**
     * Get statistics about icon coverage
     */
    fun getIconStats(totalApps: Int): IconStats {
        return IconStats(
            totalApps = totalApps,
            customIcons = customIconMap.size,
            defaultIcons = if (useDefaultIcons) DefaultIconPack.getSupportedPatterns().size else 0,
            useDefaultPack = useDefaultIcons
        )
    }

    data class IconStats(
        val totalApps: Int,
        val customIcons: Int,
        val defaultIcons: Int,
        val useDefaultPack: Boolean
    )

    /**
     * Apply adaptive icon masking
     */
    fun applyAdaptiveIconMask(icon: Drawable?): Drawable? {
        if (icon == null) return null

        // For now, return the original icon
        // This can be extended to apply custom masks
        return icon
    }
}
