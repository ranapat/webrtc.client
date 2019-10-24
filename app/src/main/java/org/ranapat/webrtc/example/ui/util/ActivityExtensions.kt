package org.ranapat.webrtc.example.ui.util

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.startRedirect(nextActivity: Class<out AppCompatActivity>, bundle: Bundle? = null) {
    val launchIntent = Intent()

    launchIntent.setClass(this, nextActivity)
    if (bundle != null) {
        launchIntent.putExtras(bundle)
    }
    startActivity(launchIntent)

    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
}

fun AppCompatActivity.startCleanRedirect(nextActivity: Class<out AppCompatActivity>, bundle: Bundle? = null) {
    val launchIntent = Intent()
    launchIntent.setClass(this, nextActivity)
    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    if (bundle != null) {
        launchIntent.putExtras(bundle)
    }

    startActivity(launchIntent)
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    finish()
}