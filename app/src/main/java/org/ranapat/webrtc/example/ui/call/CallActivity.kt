package org.ranapat.webrtc.example.ui.call

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_call.*
import org.ranapat.webrtc.example.R
import org.ranapat.webrtc.example.data.entity.Stream
import org.ranapat.webrtc.example.ui.BaseActivity
import org.ranapat.webrtc.example.ui.BaseViewModel
import org.ranapat.webrtc.example.ui.call.CallViewModel.*
import org.ranapat.webrtc.example.ui.common.IntentParameters
import org.ranapat.webrtc.example.ui.common.States.*
import org.ranapat.webrtc.example.ui.util.startCleanRedirect
import org.ranapat.webrtc.example.ui.util.subscribeUiThread

class CallActivity : BaseActivity() {
    private val PERMISSIONS_REQUEST_FOR_VIDEO = 1

    private lateinit var nextActivity: Class<out AppCompatActivity>
    private lateinit var message: String

    private val viewModel: CallViewModel by lazy { ViewModelProviders.of(this).get(CallViewModel::class.java) }

    override val layoutResource: Int = R.layout.activity_call

    override fun baseViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initialize(
                intent.extras?.getSerializable(IntentParameters.STREAM) as Stream,
                intent.extras?.getString(IntentParameters.CALL_MODE),
                localSurfaceViewRenderer,
                removeSurfaceViewRenderer
        )
    }

    override fun onPause() {
        super.onPause()

        viewModel.pause()
    }

    override fun onResume() {
        super.onResume()

        if (checkCameraPermission()) {
            viewModel.resume()
        }
    }

    override fun onDestroy() {
        viewModel.dispose()

        super.onDestroy()
    }

    override fun onBackPressed() {
        startCleanRedirect(nextActivity)
    }

    override fun initializeUi() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun initializeListeners() {
        subscription(viewModel.state
                .filter { !shallThrottle(it) }
                .subscribeUiThread(this) {
                    when (it) {
                        LOADING -> loading()
                        ERROR -> error()
                        NOT_AVAILABLE -> bindNotAvailable()
                        BUSY -> bindBusy()
                        CANCEL -> bindCancel()
                        TERMINATE -> bindTerminate()
                        RINGING -> bindRinging()
                        INITIALIZING_CALL -> bindInitializingCall()
                        CALL_ESTABLISHED -> bindCallEstablished()
                    }
                }
        )
        subscription(viewModel.message
                .subscribeUiThread(this) {
                    message = it.message
                })
        subscription(viewModel.next
                .subscribeUiThread(this) {
                    nextActivity = it
                })
        subscription(viewModel.stream
                .subscribeUiThread(this) {
                    callerNameTextView.text = it.name
                })

        buttonTextView.setOnClickListener {
            close()
        }
    }

    private fun loading() {
        bindCalling()
    }

    private fun error() {
        bindError(message)
    }

    private fun bindCalling() {
        callStatusGroup.isVisible = true
        videoView.isInvisible = true
        openDoorCardView.isVisible = false
        callStatusImageView.setImageResource(R.drawable.ic_calling)
        callStatusTextView.setText(R.string.call_status_calling)
        buttonTextView.setText(R.string.call_cancel)
    }

    private fun bindError(message: String) {
        callStatusGroup.isVisible = true
        videoView.isInvisible = true
        openDoorCardView.isVisible = false
        callStatusImageView.setImageResource(R.drawable.ic_call_not_available)
        callStatusTextView.text = message
        buttonTextView.setText(R.string.call_back)
    }

    private fun bindNotAvailable() {
        callStatusGroup.isVisible = true
        videoView.isInvisible = true
        openDoorCardView.isVisible = false
        callStatusImageView.setImageResource(R.drawable.ic_call_not_available)
        callStatusTextView.setText(R.string.call_status_not_available)
        buttonTextView.setText(R.string.call_back)
    }

    private fun bindBusy() {
        callStatusGroup.isVisible = true
        videoView.isInvisible = true
        openDoorCardView.isVisible = false
        callStatusImageView.setImageResource(R.drawable.ic_call_ended)
        callStatusTextView.setText(R.string.call_status_busy)
        buttonTextView.setText(R.string.call_back)
    }

    private fun bindCancel() {
        callStatusGroup.isVisible = true
        videoView.isInvisible = true
        openDoorCardView.isVisible = false
        callStatusImageView.setImageResource(R.drawable.ic_call_ended)
        callStatusTextView.setText(R.string.call_status_cancel)
        buttonTextView.setText(R.string.call_back)
    }

    private fun bindTerminate() {
        callStatusGroup.isVisible = true
        videoView.isInvisible = true
        openDoorCardView.isVisible = false
        callStatusImageView.setImageResource(R.drawable.ic_call_ended)
        callStatusTextView.setText(R.string.call_status_ended)
        buttonTextView.setText(R.string.call_back)
    }

    private fun bindRinging() {
        callStatusGroup.isVisible = true
        videoView.isInvisible = true
        openDoorCardView.isVisible = false
        callStatusImageView.setImageResource(R.drawable.ic_ringing)
        callStatusTextView.setText(R.string.call_status_ringing)
        buttonTextView.setText(R.string.call_back)

        callStatusImageView.setOnClickListener {
            viewModel.preCallAction()

            callStatusImageView.setOnClickListener(null)
        }
    }

    private fun bindInitializingCall() {
        callStatusGroup.isVisible = true
        videoView.isInvisible = true
        openDoorCardView.isVisible = false
        callStatusImageView.setImageResource(R.drawable.ic_ringing)
        callStatusTextView.setText(R.string.call_status_initializing_call)
        buttonTextView.setText(R.string.call_back)
    }

    private fun bindCallEstablished() {
        callStatusGroup.isVisible = true
        videoView.isInvisible = false
        openDoorCardView.isVisible = false
        callStatusImageView.setImageResource(R.drawable.ic_call_connected)
        callStatusTextView.setText(R.string.call_status_connected)
        buttonTextView.setText(R.string.call_hang_up)
    }

    private fun bindOpenDoor() {
        callStatusGroup.isVisible = false
        videoView.isInvisible = true
        openDoorCardView.isVisible = true
    }

    private fun close() {
        onBackPressed()
    }

    private fun checkCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                    PERMISSIONS_REQUEST_FOR_VIDEO
            )

            return false
        } else {
            return true
        }
    }
}