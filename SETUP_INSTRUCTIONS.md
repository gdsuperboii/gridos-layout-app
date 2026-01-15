# Custom Background and Icons Setup Guide

## 🎯 Overview
Your launcher now supports custom GIF backgrounds and image-based icons! Follow these steps to add your custom images.

## 📋 Prerequisites
1. **Sync Gradle**: Click "Sync Now" in Android Studio to download the Glide library
2. **Prepare your images**: Have your GIF and icon images ready

## 🖼️ Adding a Custom GIF Background

### Step 1: Add Your GIF File
1. Place your GIF file in: `app/src/main/res/drawable/`
2. Name it something like: `background_gif.gif`
3. Recommended size: 1080x1920 pixels or higher
4. Keep file size under 5MB for best performance

### Step 2: Enable the GIF Background
In `MainActivity.kt`, find the `setupBackground()` function and uncomment this line:
```kotlin
backgroundView.setBackgroundGif(R.drawable.background_gif)
```

Replace `background_gif` with your actual filename (without the .gif extension).

### Alternative: Use a Static Image
If you prefer a static image instead of a GIF:
```kotlin
backgroundView.setBackgroundImage(R.drawable.your_image)
```

## 🎨 Adding Custom Icon Backgrounds

### Option 1: Single Background for All Icons
1. Place your icon background image in: `app/src/main/res/drawable/`
2. Name it: `icon_bg.png` (or any name you prefer)
3. Update `item_app.xml`, change this line:
```xml
android:src="@drawable/app_icon_frame"
```
to:
```xml
android:src="@drawable/icon_bg"
```

### Option 2: Different Backgrounds Per App
In `LauncherAdapter.kt`, in the `AppViewHolder.bind()` function:
```kotlin
// Set custom background based on package name
when (appInfo.packageName) {
    "com.android.chrome" -> iconBackgroundView.setImageResource(R.drawable.chrome_bg)
    "com.spotify.music" -> iconBackgroundView.setImageResource(R.drawable.spotify_bg)
    else -> iconBackgroundView.setImageResource(R.drawable.default_icon_bg)
}
```

## 🎯 Adding Custom App Icons

### Method 1: Icon Pack System
In `MainActivity.kt`, after creating the `IconPackManager`:
```kotlin
val iconPackManager = IconPackManager(this)

// Register custom icons for specific apps
iconPackManager.registerCustomIcon("com.android.chrome", R.drawable.chrome_icon)
iconPackManager.registerCustomIcon("com.spotify.music", R.drawable.spotify_icon)
```

### Method 2: Load from External Files
```kotlin
iconPackManager.loadIconFromPath("/sdcard/icons/custom_icon.png") { drawable ->
    // Use the loaded drawable
}
```

## 📁 Recommended File Structure
```
res/
├── drawable/
│   ├── background_gif.gif          # Your animated background
│   ├── icon_bg.png                 # Default icon background
│   ├── chrome_icon.png             # Custom Chrome icon
│   ├── spotify_icon.png            # Custom Spotify icon
│   └── ...
```

## 🎨 Image Specifications

### Background GIF
- **Format**: GIF (animated)
- **Size**: 1080x1920 or 1440x2560
- **File Size**: < 5MB recommended
- **Optimization**: Use tools like ezgif.com to optimize

### Icon Backgrounds
- **Format**: PNG (with transparency)
- **Size**: 256x256 pixels
- **Style**: Rounded corners, shadows, gradients

### Custom App Icons
- **Format**: PNG (with transparency)
- **Size**: 256x256 pixels
- **Style**: Match your icon background theme

## ⚡ Performance Tips

1. **Optimize GIFs**: Use online tools to reduce file size
2. **Use WebP**: Consider WebP format for better compression
3. **Cache Images**: Glide automatically caches images
4. **Test on Device**: Always test performance on actual hardware

## 🔧 Troubleshooting

### GIF Not Animating
- Make sure you synced Gradle
- Check that the GIF file is in the correct folder
- Verify the filename matches in your code

### Icons Not Showing
- Ensure images are in `res/drawable/`
- Check that resource names don't have special characters
- Rebuild the project (Build > Rebuild Project)

### Performance Issues
- Reduce GIF file size
- Lower GIF resolution
- Use static images instead of GIFs

## 📝 Example Code

Here's a complete example in `MainActivity.kt`:

```kotlin
private fun setupBackground() {
    val backgroundView = findViewById<GifBackgroundView>(R.id.backgroundView)

    // Set animated GIF background
    backgroundView.setBackgroundGif(R.drawable.background_gif)
}

private fun setupCustomIcons() {
    val iconPackManager = IconPackManager(this)

    // Register custom icons
    iconPackManager.registerCustomIcon("com.android.chrome", R.drawable.chrome_icon)
    iconPackManager.registerCustomIcon("com.spotify.music", R.drawable.spotify_icon)
    iconPackManager.registerCustomIcon("com.whatsapp", R.drawable.whatsapp_icon)
}
```

## 🎨 Pre-Stored Default Icons

Your launcher now comes with **pre-stored default icons** for common apps! These icons are automatically applied without any setup required.

### Supported Apps (Auto-Detected)
- **Browsers**: Chrome, Firefox, Opera, Edge, Samsung Internet
- **Camera**: Camera, GCam, Open Camera
- **Music**: Spotify, YouTube Music, Pandora, SoundCloud
- **Phone**: Dialer, Phone, Contacts
- **Messaging**: WhatsApp, Telegram, Signal, Messenger, SMS
- **Email**: Gmail, Outlook, Email
- **Settings**: System Settings
- **Gallery**: Photos, Gallery, QuickPic

### How It Works
The launcher automatically detects app package names and applies matching default icons. For example:
- `com.android.chrome` → Browser icon (blue)
- `com.spotify.music` → Music icon (green)
- `com.whatsapp` → Messages icon (blue)

### Customization Options

**Disable Default Icons:**
```kotlin
// In MainActivity.kt, setupDefaultIcons()
iconPackManager.useDefaultIcons = false
```

**Override with Custom Icons:**
```kotlin
// Custom icons take priority over default icons
iconPackManager.registerCustomIcon("com.android.chrome", R.drawable.my_chrome_icon)
```

**Check Icon Coverage:**
```kotlin
val stats = iconPackManager.getIconStats(totalApps)
println("Using ${stats.defaultIcons} default icon patterns")
```

## 🎉 You're All Set!

After following these steps:
1. Sync Gradle
2. Add your images (optional - default icons work out of the box!)
3. Update the code
4. Build and run

Your launcher will now have:
- ✅ Beautiful custom GIF background
- ✅ Pre-stored default icons for common apps
- ✅ Styled icon frames
- ✅ Professional appearance
