package com.app.test.videorecording.base

import android.os.Bundle
import androidx.annotation.UiThread
import androidx.viewbinding.ViewBinding

abstract class BaseBindingActivity<VB : ViewBinding> : BaseActivity() {

    lateinit var mBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        this.mBinding = setBinding()
        setContentView(this.mBinding.root)
    }

    override fun getLayoutRes(): Int? {
        return null
    }

    @UiThread
    abstract fun setBinding(): VB
}