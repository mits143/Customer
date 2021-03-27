package com.otgs.customerapp.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import com.otgs.customerapp.R


class TextViewDrawableSize : TextView {
    private var mDrawableWidth = 0
    private var mDrawableHeight = 0

    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.TextViewDrawableSize,
            defStyleAttr,
            defStyleRes
        )
        try {
            mDrawableWidth = array.getDimensionPixelSize(
                R.styleable.TextViewDrawableSize_compoundDrawableWidth,
                -1
            )
            mDrawableHeight = array.getDimensionPixelSize(
                R.styleable.TextViewDrawableSize_compoundDrawableHeight,
                -1
            )
        } finally {
            array.recycle()
        }
        if (mDrawableWidth > 0 || mDrawableHeight > 0) {
            initCompoundDrawableSize()
        }
    }

    private fun initCompoundDrawableSize() {
        val drawables = compoundDrawables
        for (drawable in drawables) {
            if (drawable == null) {
                continue
            }
            val realBounds = drawable.bounds
            val scaleFactor = realBounds.height() / realBounds.width().toFloat()
            var drawableWidth = realBounds.width().toFloat()
            var drawableHeight = realBounds.height().toFloat()
            if (mDrawableWidth > 0) {
                // save scale factor of image
                if (drawableWidth > mDrawableWidth) {
                    drawableWidth = mDrawableWidth.toFloat()
                    drawableHeight = drawableWidth * scaleFactor
                }
            }
            if (mDrawableHeight > 0) {
                // save scale factor of image
                if (drawableHeight > mDrawableHeight) {
                    drawableHeight = mDrawableHeight.toFloat()
                    drawableWidth = drawableHeight / scaleFactor
                }
            }
            realBounds.right = realBounds.left + Math.round(drawableWidth)
            realBounds.bottom = realBounds.top + Math.round(drawableHeight)
            drawable.bounds = realBounds
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3])
    }
}
