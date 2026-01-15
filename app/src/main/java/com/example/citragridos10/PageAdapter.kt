package com.example.citragridos10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for ViewPager2 that manages multiple pages of apps and widgets
 */
class PageAdapter(
    private val onAppClick: (AppInfo) -> Unit,
    private val onAppLongClick: (AppInfo) -> Boolean = { false }
) : RecyclerView.Adapter<PageAdapter.PageViewHolder>() {

    private val pages = mutableListOf<LauncherPage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.page_layout, parent, false)
        return PageViewHolder(view, onAppClick, onAppLongClick)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size

    /**
     * Update the pages with new data
     */
    fun submitPages(newPages: List<LauncherPage>) {
        pages.clear()
        pages.addAll(newPages)
        notifyDataSetChanged()
    }

    /**
     * ViewHolder for each page
     */
    class PageViewHolder(
        itemView: View,
        private val onAppClick: (AppInfo) -> Unit,
        private val onAppLongClick: (AppInfo) -> Boolean
    ) : RecyclerView.ViewHolder(itemView) {

        private val recyclerView: RecyclerView = itemView.findViewById(R.id.pageRecyclerView)
        private val adapter: LauncherAdapter = LauncherAdapter(onAppClick, onAppLongClick)
        private val layoutManager: AlphabeticalLayoutManager

        init {
            layoutManager = AlphabeticalLayoutManager(
                spanCount = 4,
                itemSpacing = 16
            )
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }

        fun bind(page: LauncherPage) {
            adapter.submitList(page.items)
        }
    }
}
