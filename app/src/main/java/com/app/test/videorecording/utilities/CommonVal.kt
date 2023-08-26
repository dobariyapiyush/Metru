package com.app.test.videorecording.utilities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.app.test.videorecording.R
import java.util.*

@SuppressLint("RestrictedApi")
fun Context.commonDialog(
    layoutResId: Int,
    cancelable: Boolean,
    title: String,
    message: String,
    positiveClickListener: (dialog: Dialog, dialogView: View) -> Unit,
    negativeClickListener: (View) -> Unit,
    outsideTouchListener: DialogInterface.OnCancelListener? = null
) {
    val dialogView = LayoutInflater.from(this).inflate(layoutResId, null)

    val builder = AlertDialog.Builder(this)
        .setView(dialogView)
        .setCancelable(cancelable)

    val dpi = this.resources.displayMetrics.density
    builder.setView(
        dialogView,
        (19 * dpi).toInt(),
        (5 * dpi).toInt(),
        (14 * dpi).toInt(),
        (5 * dpi).toInt()
    )

    val dialog = builder.create()
    dialog.show()

    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    val positiveButton = dialogView.findViewById<TextView>(R.id.dialog_button_positive)
    val negativeButton = dialogView.findViewById<TextView>(R.id.dialog_button_negative)
    val txtTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
    val txtMessage = dialogView.findViewById<TextView>(R.id.dialog_message)

    if (title.isNotEmpty())
        txtTitle.text = title

    if (message.isNotEmpty()) {
        txtMessage.visible
        txtMessage.text = message
    }

    positiveButton.setOnClickListener {
        positiveClickListener(dialog, dialogView)
    }

    negativeButton.setOnClickListener {
        negativeClickListener(dialogView)
        dialog.dismiss()
    }

    dialog.setCanceledOnTouchOutside(cancelable)
    dialog.setOnCancelListener(outsideTouchListener)
}

private var lastClickTime: Long = 0

fun singleClick(action: () -> Unit) {
    if (SystemClock.elapsedRealtime() - lastClickTime < 1500L) {
        return
    }
    lastClickTime = SystemClock.elapsedRealtime()
    action.invoke()
}
