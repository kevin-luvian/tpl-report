package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R

class SwipeHelper(private val swipeCallback: SwipeCallback) : ItemTouchHelper.Callback() {
    private var swipeBack = false
    private var showButton = false
    private val buttonWidth = 240F
    private val iconSize = 50
    private var buttonDeleteInstance: RectF? = null
    private var buttonEditInstance: RectF? = null
    private var currentViewHolder: RecyclerView.ViewHolder? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Do Nothing
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return ItemTouchHelper.ACTION_STATE_IDLE
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        onDraw(c)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (showButton) {
                val newdX = dX.coerceAtMost(-buttonWidth * 2)
                super.onChildDraw(
                    c, recyclerView, viewHolder, newdX, dY, actionState, isCurrentlyActive
                )
            } else {
                setTouchListener(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )
            }
        }
        if (!showButton) super.onChildDraw(
            c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
        )
        currentViewHolder = viewHolder
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            swipeBack =
                event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
//            Log.d("Onswipe", swipeBack.toString())
//            Log.d("dX", dX.toString())
            if (swipeBack) {
                if (dX <= -buttonWidth * 2) {
                    showButton = true
                }
                if (showButton) {
                    Log.d("RecyclerState", "showing button")
                    setTouchUpListener(
                        c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                    )
                    setItemsClickable(recyclerView, false)
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Log.d("Ontouch", "touch down")
                setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                Log.d("Ontouch", "touch up")
                onButtonClick(viewHolder, event)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    0F,
                    dY,
                    actionState,
                    false
                )
                recyclerView.setOnTouchListener { _, _ -> false }
                setItemsClickable(recyclerView, true)
                swipeBack = true
                showButton = false
            }
            true
        }
    }

    private fun onButtonClick(v: RecyclerView.ViewHolder, event: MotionEvent) {
        buttonDeleteInstance?.let {
            if (it.contains(event.x, event.y)) {
                swipeCallback.onDeleteClicked(v.adapterPosition)
            }
        }
        buttonEditInstance?.let {
            if (it.contains(event.x, event.y)) {
                swipeCallback.onEditClicked(v.adapterPosition)
            }
        }
        buttonDeleteInstance = null
        buttonEditInstance = null
    }

    private fun setItemsClickable(
        recyclerView: RecyclerView,
        isClickable: Boolean
    ) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    fun onDraw(c: Canvas) {
        currentViewHolder?.let { drawButtons(c, it) }
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val ctx = viewHolder.itemView.context
        val buttonWidthWithMargin = buttonWidth - 20F
        val corners = 40F
        val itemView: View = viewHolder.itemView
        val deleteButtRect = RectF(
            itemView.right - buttonWidthWithMargin,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat()
        )
        val paint = Paint()
        paint.color = ContextCompat.getColor(ctx, R.color.dark_primary)
        c.drawRoundRect(deleteButtRect, corners, corners, paint)
        drawIcon(c, ctx, deleteButtRect, ContextCompat.getDrawable(ctx, R.drawable.ic_delete)!!)
        buttonDeleteInstance = deleteButtRect

        val editButtRect = RectF(
            itemView.right - buttonWidth - buttonWidthWithMargin,
            itemView.top.toFloat(),
            itemView.right - buttonWidth,
            itemView.bottom.toFloat()
        )
        c.drawRoundRect(editButtRect, corners, corners, paint)
        drawIcon(c, ctx, editButtRect, ContextCompat.getDrawable(ctx, R.drawable.ic_edit)!!)
        buttonEditInstance = editButtRect
    }

    private fun drawIcon(c: Canvas, ctx: Context, button: RectF, d: Drawable) {
        val color = ContextCompat.getColor(ctx, R.color.light_primary)
        d.setBounds(
            button.centerX().toInt() - iconSize,
            button.centerY().toInt() - iconSize,
            button.centerX().toInt() + iconSize,
            button.centerY().toInt() + iconSize
        )
        d.setTint(color)
        d.draw(c)
    }
}

interface SwipeCallback {
    fun onDeleteClicked(position: Int)
    fun onEditClicked(position: Int)
}