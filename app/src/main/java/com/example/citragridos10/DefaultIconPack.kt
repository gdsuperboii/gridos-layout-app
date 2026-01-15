package com.example.citragridos10

/**
 * Pre-stored default icon pack with mappings for common apps
 */
object DefaultIconPack {

    /**
     * Map of package name patterns to default icon resources
     */
    private val iconMappings = mapOf(
        // Browsers
        "chrome" to R.drawable.default_icon_browser,
        "browser" to R.drawable.default_icon_browser,
        "firefox" to R.drawable.default_icon_browser,
        "opera" to R.drawable.default_icon_browser,
        "edge" to R.drawable.default_icon_browser,
        "samsung.internet" to R.drawable.default_icon_browser,

        // Camera
        "camera" to R.drawable.default_icon_camera,
        "gcam" to R.drawable.default_icon_camera,
        "opencamera" to R.drawable.default_icon_camera,

        // Music & Audio
        "music" to R.drawable.default_icon_music,
        "spotify" to R.drawable.default_icon_music,
        "soundcloud" to R.drawable.default_icon_music,
        "youtube.music" to R.drawable.default_icon_music,
        "pandora" to R.drawable.default_icon_music,
        "audioplayer" to R.drawable.default_icon_music,

        // Phone & Dialer
        "dialer" to R.drawable.default_icon_phone,
        "phone" to R.drawable.default_icon_phone,
        "contacts" to R.drawable.default_icon_phone,

        // Messaging
        "messaging" to R.drawable.default_icon_messages,
        "messages" to R.drawable.default_icon_messages,
        "sms" to R.drawable.default_icon_messages,
        "mms" to R.drawable.default_icon_messages,
        "whatsapp" to R.drawable.default_icon_messages,
        "telegram" to R.drawable.default_icon_messages,
        "signal" to R.drawable.default_icon_messages,
        "messenger" to R.drawable.default_icon_messages,

        // Email
        "email" to R.drawable.default_icon_email,
        "gmail" to R.drawable.default_icon_email,
        "outlook" to R.drawable.default_icon_email,
        "mail" to R.drawable.default_icon_email,

        // Settings
        "settings" to R.drawable.default_icon_settings,
        "systemui" to R.drawable.default_icon_settings,

        // Gallery & Photos
        "gallery" to R.drawable.default_icon_gallery,
        "photos" to R.drawable.default_icon_gallery,
        "gallery3d" to R.drawable.default_icon_gallery,
        "quickpic" to R.drawable.default_icon_gallery
    )

    /**
     * Get default icon resource for a package name
     * Returns null if no matching icon is found
     */
    fun getIconForPackage(packageName: String): Int? {
        val lowerPackage = packageName.lowercase()

        // Try exact match first
        iconMappings.forEach { (key, iconRes) ->
            if (lowerPackage.contains(key)) {
                return iconRes
            }
        }

        // Return generic app icon as fallback
        return R.drawable.default_icon_app
    }

    /**
     * Check if a default icon exists for this package
     */
    fun hasIconForPackage(packageName: String): Boolean {
        val lowerPackage = packageName.lowercase()
        return iconMappings.keys.any { lowerPackage.contains(it) }
    }

    /**
     * Get all supported package patterns
     */
    fun getSupportedPatterns(): Set<String> {
        return iconMappings.keys
    }
}
