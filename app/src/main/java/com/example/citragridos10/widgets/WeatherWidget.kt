package com.example.citragridos10.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.example.citragridos10.R

/**
 * Custom Weather Widget for the launcher
 */
class WeatherWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val temperatureTextView: TextView
    private val conditionTextView: TextView
    private val locationTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_weather, this, true)
        temperatureTextView = findViewById(R.id.temperatureText)
        conditionTextView = findViewById(R.id.conditionText)
        locationTextView = findViewById(R.id.locationText)

        // Set default values (in a real app, you'd fetch actual weather data)
        updateWeather("72°F", "Partly Cloudy", "San Francisco")
    }

    fun updateWeather(temperature: String, condition: String, location: String) {
        temperatureTextView.text = temperature
        conditionTextView.text = condition
        locationTextView.text = location
    }
}
