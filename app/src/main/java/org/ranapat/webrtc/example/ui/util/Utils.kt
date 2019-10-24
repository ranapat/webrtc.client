package org.ranapat.webrtc.example.ui.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import org.ranapat.webrtc.example.BuildConfig
import timber.log.Timber
import java.util.*

fun Context.openAppPlayStore() {
    try {
        val intent = appPlayStoreIntent()
        startActivity(intent)
    } catch (exception: ActivityNotFoundException) {
        Timber.e(exception)
    }
}

fun appPlayStoreIntent(): Intent {
    return Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(
                "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
        setPackage("com.android.vending")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}

inline fun Activity.showActionDialog(title: String,
                                     description: String,
                                     okButton: String = getString(android.R.string.ok),
                                     cancelButton: String = getString(android.R.string.cancel),
                                     crossinline action: () -> Unit) {
    AlertDialog.Builder(this).run {
        setCancelable(true)
        setTitle(title)
        setMessage(description)
        setPositiveButton(okButton) { _, _ ->
            action.invoke()
        }
        setNegativeButton(cancelButton) { _, _ ->
        }
        create()
        show()
    }
}

fun getStringForLocale(context: Context, @StringRes stringId: Int, locale: String): String? {
    val configuration = Configuration(context.resources.configuration)
    configuration.setLocale(Locale(locale))
    return context
            .createConfigurationContext(configuration)
            .resources
            .getString(stringId)
}