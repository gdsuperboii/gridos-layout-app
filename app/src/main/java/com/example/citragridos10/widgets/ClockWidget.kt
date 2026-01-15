package com.example.citragridos10.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.example.citragridos10.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Custom Clock Widget for the launcher
 */
class ClockWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val timeTextView: TextView
    private val dateTextView: TextView
    private val updateRunnable: Runnable
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_clock, this, true)
        timeTextView = findViewById(R.id.timeText)
        dateTextView = findViewById(R.id.dateText)

        updateRunnable = object : Runnable {
            override fun run() {
                updateTime()
                postDelayed(this, 1000) // Update every second
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post(updateRunnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(updateRunnable)
    }

    private fun updateTime() {
        val now = Date()
        timeTextView.text = timeFormat.format(now)
        dateTextView.text = dateFormat.format(now)
    }
}
