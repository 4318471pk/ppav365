package com.live.fox.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.effective.android.panel.view.panel.IPanelView
import com.live.fox.R

class CusPanelView : IPanelView, AppCompatTextView {

    private var triggerViewId = 0
    private var isToggle = true

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        initView(attrs, defStyleAttr, 0)
    }

    private fun initView(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CusPanelView, defStyleAttr, defStyleRes)
        triggerViewId = typedArray.getResourceId(R.styleable.CusPanelView_panel_trigger, -1)
        isToggle = typedArray.getBoolean(R.styleable.CusPanelView_panel_toggle, isToggle)
        typedArray.recycle()
        setBackgroundColor(Color.BLACK)
        text = "自定义面板"
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        textSize = 20f
    }

    override fun assertView() {
        TODO("Not yet implemented")
    }

    override fun getBindingTriggerViewId(): Int = triggerViewId

    override fun isTriggerViewCanToggle(): Boolean = isToggle

    override fun isShowing(): Boolean = isShown


    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}