package com.app.test.videorecording.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {

    val TAG: String = javaClass.simpleName

    val mActivity: BaseActivity
        get() {
            return getActivityContext()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)

        getLayoutRes()?.let {
            setContentView(it)
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setContentView()
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        setContentView()
    }

    private fun setContentView() {
        initAds()
        initView()
        initViewAction()
        initViewListener()
    }

    @UiThread
    @LayoutRes
    abstract fun getLayoutRes(): Int?

    @UiThread
    open fun initView() {
    }

    @UiThread
    open fun initAds() {
    }

    @UiThread
    open fun initViewAction() {
    }

    @UiThread
    open fun initViewListener() {
    }

    override fun onClick(v: View) {

    }

    @UiThread
    open fun setClickListener(vararg fViews: View) {
        for (lView in fViews) {
            lView.setOnClickListener(this)
        }
    }

    @UiThread
    abstract fun getActivityContext(): BaseActivity
}