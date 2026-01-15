package com.example.citragridos10

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max

/**
 * Custom LayoutManager that arranges apps alphabetically from A-Z
 * with support for widgets that can span multiple cells
 */
class AlphabeticalLayoutManager(
    private val spanCount: Int = 4,
    private val itemSpacing: Int = 16
) : RecyclerView.LayoutManager() {

    private var totalHeight = 0
    private var totalWidth = 0
    private val viewCache = mutableMapOf<Int, Rect>()

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            return
        }

        if (childCount == 0 && state.isPreLayout) {
            return
        }

        detachAndScrapAttachedViews(recycler)
        viewCache.clear()

        val availableWidth = width - paddingLeft - paddingRight
        val cellWidth = (availableWidth - (spanCount - 1) * itemSpacing) / spanCount
        val cellHeight = cellWidth // Square cells for apps

        var currentX = paddingLeft
        var currentY = paddingTop
        var currentColumn = 0
        var maxRowHeight = cellHeight

        // Layout all items
        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)

            // Determine if this is a widget (check view tag or adapter item type)
            val spanX = getItemSpanX(view, i)
            val spanY = getItemSpanY(view, i)

            // Check if widget fits in current row
            if (currentColumn + spanX > spanCount) {
                // Move to next row
                currentY += maxRowHeight + itemSpacing
                currentX = paddingLeft
                currentColumn = 0
                maxRowHeight = cellHeight
            }

            val itemWidth = cellWidth * spanX + itemSpacing * (spanX - 1)
            val itemHeight = cellHeight * spanY + itemSpacing * (spanY - 1)

            // Measure child with exact dimensions
            measureChildWithMargins(view,
                width - itemWidth,
                height - itemHeight)

            val left = currentX
            val top = currentY
            val right = left + itemWidth
            val bottom = top + itemHeight

            layoutDecorated(view, left, top, right, bottom)
            viewCache[i] = Rect(left, top, right, bottom)

            // Update position for next item
            currentColumn += spanX
            currentX += itemWidth + itemSpacing
            maxRowHeight = max(maxRowHeight, itemHeight)

            // Move to next row if we've filled this one
            if (currentColumn >= spanCount) {
                currentY += maxRowHeight + itemSpacing
                currentX = paddingLeft
                currentColumn = 0
                maxRowHeight = cellHeight
            }
        }

        totalHeight = currentY + maxRowHeight + paddingBottom
        totalWidth = width
    }

    /**
     * Get horizontal span for an item (widgets can span multiple columns)
     */
    private fun getItemSpanX(view: View, position: Int): Int {
        return (view.tag as? WidgetInfo)?.spanX ?: 1
    }

    /**
     * Get vertical span for an item (widgets can span multiple rows)
     */
    private fun getItemSpanY(view: View, position: Int): Int {
        return (view.tag as? WidgetInfo)?.spanY ?: 1
    }

    override fun canScrollVertically(): Boolean = true

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        if (childCount == 0 || dy == 0) {
            return 0
        }

        // Calculate current scroll position
        val firstChild = getChildAt(0) ?: return 0
        val currentScroll = paddingTop - firstChild.top

        // Calculate scroll limits
        val maxScroll = kotlin.math.max(0, totalHeight - height)

        // Determine actual scroll amount
        val scrolled = when {
            dy > 0 -> {
                // Scrolling down - limit to maxScroll
                kotlin.math.min(dy, maxScroll - currentScroll)
            }
            else -> {
                // Scrolling up - limit to 0
                kotlin.math.max(dy, -currentScroll)
            }
        }

        // Apply the scroll
        if (scrolled != 0) {
            offsetChildrenVertical(-scrolled)
        }

        return scrolled
    }

    override fun computeVerticalScrollExtent(state: RecyclerView.State): Int {
        return height
    }

    override fun computeVerticalScrollOffset(state: RecyclerView.State): Int {
        if (childCount == 0) {
            return 0
        }
        val firstChild = getChildAt(0) ?: return 0
        return paddingTop - firstChild.top
    }

    override fun computeVerticalScrollRange(state: RecyclerView.State): Int {
        return totalHeight
    }

    /**
     * Find the position to insert a widget at specific grid coordinates
     */
    fun findPositionForWidget(gridX: Int, gridY: Int): Int {
        // This would be used when adding widgets dynamically
        // For now, widgets are added to the end of the list
        return itemCount
    }

    /**
     * Check if a widget can be placed at the given position
     */
    fun canPlaceWidget(gridX: Int, gridY: Int, spanX: Int, spanY: Int): Boolean {
        if (gridX + spanX > spanCount) return false
        // Additional collision detection would go here
        return true
    }
}
