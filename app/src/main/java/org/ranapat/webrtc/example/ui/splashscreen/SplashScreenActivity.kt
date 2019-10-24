package org.ranapat.webrtc.example.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProviders
import org.ranapat.webrtc.example.R
import org.ranapat.webrtc.example.ui.BaseActivity
import org.ranapat.webrtc.example.ui.common.States.*
import org.ranapat.webrtc.example.ui.util.subscribeUiThread

class SplashScreenActivity : BaseActivity() {
    private lateinit var viewModel: SplashScreenViewModel
    private lateinit var nextActivity: Class<*>

    private var message: String = ""

    override val layoutResource: Int = R.layout.activity_splash_screen
    override fun baseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SplashScreenViewModel::class.java)

        super.onCreate(savedInstanceState)

        viewModel.initialize()
    }

    override fun initializeUi() {
        //
    }

    override fun initializeListeners() {
        subscription(viewModel.message
                .subscribeUiThread(this) {
                    message = it.message
                })

        subscription(viewModel.state
                .subscribeUiThread(this) {
                    when (it) {
                        LOADING -> loading()
                        ERROR -> error(message)
                        COMPLETE -> complete()
                        REDIRECT -> redirect()
                    }
                })
        subscription(viewModel.next
                .subscribeUiThread(this) {
                    nextActivity = it
                })
    }

    private fun loading() {
        var fragmentTransaction = supportFragmentManager.beginTransaction()

        val loading = LoadingFragment()

        fragmentTransaction.replace(R.id.fragmentContainer, loading)
        fragmentTransaction.commit()
    }

    private fun error(message: String?) {
        var fragmentTransaction = supportFragmentManager.beginTransaction()

        val error = ErrorMessageFragment()

        var bundle = Bundle()
        bundle.putString("message", message)
        error.arguments = bundle

        fragmentTransaction.replace(R.id.fragmentContainer, error)
        fragmentTransaction.commit()
    }

    private fun complete() {
        startActivityDelayed(500, nextActivity, Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private fun redirect() {
        startActivityDelayed(500, nextActivity)
    }

    private fun startActivityDelayed(delay: Long, activity: Class<*>, flags: Int? = null) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFinishing) {
                val launchIntent = Intent()
                launchIntent.setClass(applicationContext, activity)
                if (flags != null) {
                    launchIntent.addFlags(flags)
                }
                startActivity(launchIntent)

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                finish()
            }
        }, delay)
    }
}
