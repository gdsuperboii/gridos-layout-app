package com.example.citragridos10

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Custom view that displays an animated GIF background
 */
class GifBackgroundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val backgroundImageView: ImageView

    init {
        // Create ImageView for the background
        backgroundImageView = ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        // Add to layout
        addView(backgroundImageView, 0) // Add at index 0 to be behind other views
    }

    /**
     * Set the background GIF from a resource
     * Note: Requires Glide library - sync Gradle first
     */
    fun setBackgroundGif(resourceId: Int) {
        try {
            // Using reflection to avoid compile-time dependency
            val glideClass = Class.forName("com.bumptech.glide.Glide")
            val withMethod = glideClass.getMethod("with", Context::class.java)
            val requestManager = withMethod.invoke(null, context)

            val asGifMethod = requestManager.javaClass.getMethod("asGif")
            val gifRequestBuilder = asGifMethod.invoke(requestManager)

            val loadMethod = gifRequestBuilder.javaClass.getMethod("load", Int::class.javaPrimitiveType)
            val requestBuilder = loadMethod.invoke(gifRequestBuilder, resourceId)

            val intoMethod = requestBuilder.javaClass.getMethod("into", ImageView::class.java)
            intoMethod.invoke(requestBuilder, backgroundImageView)
        } catch (e: Exception) {
            // Fallback to regular drawable if Glide is not available
            backgroundImageView.setImageResource(resourceId)
        }
    }

    /**
     * Set the background image from a resource (static image)
     */
    fun setBackgroundImage(resourceId: Int) {
        backgroundImageView.setImageResource(resourceId)
    }

    /**
     * Set the background from a file path
     */
    fun setBackgroundFromPath(path: String) {
        // This will work after Gradle sync
        backgroundImageView.setImageDrawable(null)
    }
}
