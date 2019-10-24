package org.ranapat.webrtc.example.ui

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.ranapat.webrtc.example.dependencies.InstanceFactory
import org.ranapat.webrtc.example.management.NetworkManager

abstract class BaseActivity @JvmOverloads constructor(
        private val networkManager: NetworkManager = InstanceFactory.get(NetworkManager::class.java)
) : AppCompatActivity() {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    protected abstract val layoutResource: Int
    protected abstract fun baseViewModel(): BaseViewModel?
    protected abstract fun initializeUi()
    protected abstract fun initializeListeners()

    private var networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            baseViewModel()?.onNetworkStatus()
        }

        override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
                baseViewModel()?.onNetworkStatus()
            }
        }

        override fun onLost(network: Network?) {
            super.onLost(network)

            baseViewModel()?.onNetworkStatus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutResource)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        networkManager.registerNetworkChangeCallback(networkCallback)

        initializeUi()
        initializeListeners()

    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
        networkManager.unregisterNetworkChangeCallback(networkCallback)
    }

    protected fun subscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

}