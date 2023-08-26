package com.app.test.videorecording.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.media3.common.Player.REPEAT_MODE_OFF
import com.app.test.videorecording.R
import com.app.test.videorecording.base.BaseActivity
import com.app.test.videorecording.base.BaseBindingActivity
import com.app.test.videorecording.databinding.ActivityMainBinding
import com.app.test.videorecording.utilities.gone
import com.app.test.videorecording.utilities.visible
import com.app.test.videorecording.widget.SampleGLView
import com.daasuu.camerarecorder.CameraRecordListener
import com.daasuu.camerarecorder.CameraRecorder
import com.daasuu.camerarecorder.CameraRecorderBuilder
import com.daasuu.camerarecorder.LensFacing
import com.devbrackets.android.exomedia.core.video.scale.ScaleType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private var counter = 6
    private val delay: Long = 1000
    private lateinit var countDownTimer: CountDownTimer
    private val scope = CoroutineScope(Dispatchers.Main)

    private var isPlayedOnce = false
    private var isPlaying = false
    private var isRecording = false

    private var width = 720
    private var height = 1280
    private var toggleClick = false
    private var filepath: String = ""
    private var sampleGLView: SampleGLView? = null
    private var cameraRecorder: CameraRecorder? = null

    override fun setBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@MainActivity
    }

    override fun initView() {
        super.initView()
        with(mBinding) {
            buttonStartStop.setBackgroundResource(R.drawable.button_dark_green)
            buttonPlayPause(false, resources.getText(R.string.play).toString())
        }
    }

    override fun initViewListener() {
        super.initViewListener()
        setClickListener(
            mBinding.buttonStartStop,
            mBinding.buttonPlayPause
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        with(mBinding) {
            when (v) {
                buttonStartStop -> {
                    when (isRecording) {
                        true -> stopRecording()
                        false -> performStart()
                    }
                }

                buttonPlayPause -> {
                    when (isPlaying) {
                        true -> pauseRecording()
                        false -> playRecording()
                    }
                }
            }
        }
    }

    private fun performStart() {
        with(mBinding) {
            viewQuestion.gone
            buttonStartStop(false, resources.getText(R.string.please_wait).toString())

            txtSmallDescription.visible
            val str = resources.getText(R.string.question)
            txtSmallDescription.text = str.toString().replace("\n", " ")
        }
        scope.launch {
            delay(delay / 2)
            timerStart()
        }
    }

    private fun startRecording() {
        isRecording = true
        isPlayedOnce = false
        filepath = getVideoFilePath()
        cameraRecorder?.start(filepath)
        start30Countdown()
    }

    private fun stopRecording() {
        countDownTimer.cancel()
        cameraRecorder?.stop()
        with(mBinding) {
            val slideOutTopAnimation =
                AnimationUtils.loadAnimation(
                    this@MainActivity,
                    R.anim.slide_out_top
                )
            slideOutTopAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    txtTimer30Second.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
            txtTimer30Second.startAnimation(slideOutTopAnimation)

            portraitFrameLayout.gone
            txtSmallDescription.gone
            viewResult.visible
            videoView.setPreviewImage(Uri.parse(filepath))

            buttonStartStop(false, "")
            buttonPlayPause(true, "")
        }
    }

    private fun playRecording() {
        with(mBinding) {
            isPlaying = true
            buttonPlayPause.text =
                resources.getText(R.string.pause)
            mBinding.viewResult.gone
            if (!isPlayedOnce) {
                videoView.setMedia(Uri.parse(filepath))
                videoView.setScaleType(ScaleType.CENTER_CROP)
                videoView.setRepeatMode(REPEAT_MODE_OFF)
                videoView.requestFocus()
                videoView.setOnPreparedListener { videoView.start() }
                isPlayedOnce = true
            } else
                videoView.start()
            if (isPlaying) {
                videoView.setOnCompletionListener {
                    isPlaying = false
                    isPlayedOnce = false
                    mBinding.buttonPlayPause.text = resources.getText(R.string.play)
                }
            }
        }
    }

    private fun pauseRecording() {
        isPlaying = false
        mBinding.buttonPlayPause.text =
            resources.getText(R.string.play)
        mBinding.videoView.pause()
    }

    private fun timerStart() {
        with(mBinding) {
            scope.launch {
                while (counter > 0) {
                    counter--
                    txtTimer5Second.text = counter.toString()
                    val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)
                    fadeOutAnimation.duration = delay
                    txtTimer5Second.startAnimation(fadeOutAnimation)
                    delay(1000)
                    if (counter == 1) {
                        val maskFadeOutAnimation = AlphaAnimation(1.0f, 0.0f)
                        maskFadeOutAnimation.duration = delay
                        imgMask.startAnimation(fadeOutAnimation)
                        val deepBreathFadeOutAnimation = AlphaAnimation(1.0f, 0.0f)
                        deepBreathFadeOutAnimation.duration = delay
                        imgDeepBreath.startAnimation(deepBreathFadeOutAnimation)
                    }
                }
                imgMask.gone
                imgDeepBreath.gone
                startRecording()
                txtTimer5Second.visibility = TextView.INVISIBLE
                buttonStartStop(true, resources.getText(R.string.stop).toString())
            }
        }
    }

    private fun buttonStartStop(status: Boolean, title: String) {
        if (title.isNotEmpty())
            mBinding.buttonStartStop.text = title
        mBinding.buttonStartStop.isClickable = status
        mBinding.buttonStartStop.isEnabled = status
        if (status)
            mBinding.buttonStartStop.setBackgroundResource(R.drawable.button_dark_green)
        else
            mBinding.buttonStartStop.setBackgroundResource(R.drawable.button_grey)
    }

    private fun buttonPlayPause(status: Boolean, title: String) {
        if (title.isNotEmpty())
            mBinding.buttonPlayPause.text = title
        mBinding.buttonPlayPause.isClickable = status
        mBinding.buttonPlayPause.isEnabled = status
        if (status)
            mBinding.buttonPlayPause.setBackgroundResource(R.drawable.button_dark_green)
        else
            mBinding.buttonPlayPause.setBackgroundResource(R.drawable.button_grey)
    }

    private fun start30Countdown() {
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val minutes = secondsRemaining / 60
                val seconds = secondsRemaining % 60
                val formattedTime = String.format("%02d:%02d", minutes, seconds)
                mBinding.txtTimer30Second.text = formattedTime
            }

            override fun onFinish() {
                stopRecording()
            }
        }
        countDownTimer.start()
    }

    private fun releaseCamera() {
        sampleGLView?.onPause()

        cameraRecorder?.apply {
            stop()
            release()
        }
        cameraRecorder = null

        sampleGLView?.let {
            mBinding.portraitFrameLayout.removeView(it)
        }
        sampleGLView = null
    }

    private fun setUpCameraView() {
        runOnUiThread {
            mBinding.portraitFrameLayout.removeAllViews()
            sampleGLView = SampleGLView(applicationContext)
            val touchListener = object : SampleGLView.TouchListener {
                override fun onTouch(event: MotionEvent, width: Int, height: Int) {
                    cameraRecorder?.changeManualFocusPoint(event.x, event.y, width, height)
                }
            }
            sampleGLView!!.setTouchListener(touchListener)
            mBinding.portraitFrameLayout.addView(sampleGLView)
        }
    }

    private fun setUpCamera() {
        setUpCameraView()

        cameraRecorder = CameraRecorderBuilder(this, sampleGLView)
            .cameraRecordListener(object : CameraRecordListener {
                override fun onGetFlashSupport(flashSupport: Boolean) {}

                override fun onRecordComplete() {
                    exportMp4ToGallery(applicationContext, filepath)
                }

                override fun onRecordStart() {}

                override fun onError(exception: Exception) {
                    Log.e("CameraRecorder", exception.toString())
                }

                override fun onCameraThreadFinish() {
                    if (toggleClick) {
                        runOnUiThread {
                            setUpCamera()
                        }
                    }
                    toggleClick = false
                }
            })
            .videoSize(width, height)
            .cameraSize(width, height)
            .lensFacing(LensFacing.FRONT)
            .build()
    }

    fun exportMp4ToGallery(context: Context, filePath: String) {
        val values = ContentValues().apply {
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.DATA, filePath)
        }
        context.contentResolver.insert(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            values
        )
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://$filePath")
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun getVideoFilePath(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val filename = "$timeStamp-cameraRecorder.mp4"
        val moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        return File(moviesDir, filename).toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

    override fun onResume() {
        super.onResume()
        setUpCamera()
    }

    override fun onStop() {
        super.onStop()
        releaseCamera()
    }
}