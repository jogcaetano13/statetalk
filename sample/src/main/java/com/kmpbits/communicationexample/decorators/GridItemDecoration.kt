package com.kmpbits.communicationexample.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(
    private val spacing: Int,
    private val includeEdges: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val position = parent.getChildAdapterPosition(view)

        if (position >= 0) {
            val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: return

            val column = position % spanCount

            if (includeEdges) {
                outRect.also {
                    it.left = spacing - column * spacing / spanCount
                    it.right = (column + 1) * spacing / spanCount

                    // Top edge
                    if (position < spanCount)
                        it.top = spacing

                    // Bottom edge
                    it.bottom = spacing

                }
            } else {
                outRect.set(0, 0, 0, 0)
            }
        }
    }
}