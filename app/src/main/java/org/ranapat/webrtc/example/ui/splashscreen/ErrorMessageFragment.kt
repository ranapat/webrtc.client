package org.ranapat.webrtc.example.ui.splashscreen

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.ranapat.webrtc.example.R
import kotlinx.android.synthetic.main.fragment_error_message.view.*

class ErrorMessageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_error_message, container, false)

        view.message.text = arguments!!.getString("message", "")
        view.retry.setOnClickListener {
            val intent = Intent()
            intent.action = ConnectivityManager.EXTRA_NETWORK_TYPE

            activity?.sendBroadcast(intent)
        }

        return view
    }
}
