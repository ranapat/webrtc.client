package org.ranapat.webrtc.example.ui.util

import android.annotation.TargetApi
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build

class AudioFocus(private val audioManager: AudioManager) {

    private val focusRequest by lazy { getAudioRequest() }
    private val afChangeListener by lazy { AudioManager.OnAudioFocusChangeListener { } }

    @TargetApi(Build.VERSION_CODES.O)
    private fun getAudioRequest() =
            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
                setAudioAttributes(AudioAttributes.Builder().run {
                    setUsage(AudioAttributes.USAGE_MEDIA)
                    setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    build()
                })
                build()
            }

    /**
     * Request audio focus immediately. It's not guaranteed to get the focus.
     * Apps that are in foreground in parallel with your app, can get the focus after you have gained it.
     * To handle these behaviours properly you need to implement a callback
     */
    @Suppress("DEPRECATION")
    fun requestAudioFocus() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> audioManager.requestAudioFocus(focusRequest)
            else -> audioManager.requestAudioFocus(
                    afChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    /**
     * Release the audio focus
     */
    @Suppress("DEPRECATION")
    fun releaseAudioFocus() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> audioManager.abandonAudioFocusRequest(focusRequest)
            else -> audioManager.abandonAudioFocus(afChangeListener)
        }
    }

}