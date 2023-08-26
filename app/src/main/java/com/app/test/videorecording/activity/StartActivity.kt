package com.app.test.videorecording.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.test.videorecording.base.BaseActivity
import com.app.test.videorecording.base.BaseBindingActivity
import com.app.test.videorecording.databinding.ActivityStartBinding
import com.app.test.videorecording.utilities.PermissionUtils.recordingPermissions
import com.app.test.videorecording.utilities.PermissionUtils.requestCodeRecording
import com.app.test.videorecording.utilities.PermissionUtils.requestCodeSetting
import com.app.test.videorecording.utilities.PermissionUtils.showPermissionRationale

class StartActivity : BaseBindingActivity<ActivityStartBinding>() {

    override fun setBinding(): ActivityStartBinding = ActivityStartBinding.inflate(layoutInflater)

    override fun getActivityContext(): BaseActivity {
        return this@StartActivity
    }

    override fun initView() {
        super.initView()
        mBinding.buttonStart.setOnClickListener { checkPermission() }
    }

    private fun checkPermission() {
        val permissionsToRequest = recordingPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                requestCodeRecording
            )
        } else {
            getStarted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeRecording) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getStarted()
            } else {
                showPermissionRationale(applicationContext)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == requestCodeSetting) {
            getStarted()
        }
    }

    private fun getStarted() {
        startActivity(Intent(this@StartActivity, MainActivity::class.java).setAction(""))
    }
}
