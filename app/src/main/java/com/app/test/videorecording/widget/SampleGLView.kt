package com.app.test.videorecording.widget

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SampleGLView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs), View.OnTouchListener {

    private var touchListener: TouchListener? = null

    init {
        setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event?.let {
            val actionMasked = it.actionMasked
            if (actionMasked != MotionEvent.ACTION_DOWN) {
                return false
            }

            touchListener?.onTouch(it, v?.width ?: 0, v?.height ?: 0)
        }
        return false
    }

    interface TouchListener {
        fun onTouch(event: MotionEvent, width: Int, height: Int)
    }

    fun setTouchListener(listener: TouchListener?) {
        touchListener = listener
    }
}