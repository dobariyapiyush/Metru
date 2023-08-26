package com.app.test.videorecording.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.app.test.videorecording.R


object PermissionUtils {

    const val requestCodeRecording: Int = 1
    const val requestCodeSetting: Int = 2

    val recordingPermissions = if (isTiramisuDevice()) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

    fun showPermissionRationale(
        context: Context
    ) {
        if (context is Activity) {
            context.commonDialog(
                layoutResId = R.layout.dialog_permission,
                cancelable = true,
                title = context.getString(R.string.permission_rationale_title),
                message = context.getString(R.string.permission_rationale_message),
                positiveClickListener = { dialog, _ ->
                    dialog.dismiss()
                    openAppSettings(context)
                },
                negativeClickListener = {

                },
                outsideTouchListener = {

                }
            )
        }
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        if (context is Activity) {
            context.startActivityForResult(intent, requestCodeSetting)
        }
    }

    private fun isTiramisuDevice(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }
}

