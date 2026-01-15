package com.example.citragridos10.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import com.example.citragridos10.R

/**
 * Custom Search Widget for the launcher
 */
class SearchWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val searchEditText: EditText

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.widget_search, this, true)
        searchEditText = view.findViewById(R.id.searchEditText)
    }

    fun setOnSearchListener(listener: (String) -> Unit) {
        searchEditText.setOnEditorActionListener { _, _, _ ->
            listener(searchEditText.text.toString())
            true
        }
    }
}
