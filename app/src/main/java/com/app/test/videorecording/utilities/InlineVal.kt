package com.app.test.videorecording.utilities

import android.view.View

inline val View.visible: View
    get() {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        return this
    }

inline val View.invisible: View
    get() {
        if (visibility != View.INVISIBLE) {
            visibility = View.INVISIBLE
        }
        return this
    }

inline val View.gone: View
    get() {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
        return this
    }

inline val View.enable: View
    get() {
        isEnabled = true
        return this
    }

inline val View.disable: View
    get() {
        isEnabled = false
        return this
    }