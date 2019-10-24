package org.ranapat.webrtc.example;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.android.gms.security.ProviderInstaller;

import org.jetbrains.annotations.Contract;
import org.ranapat.webrtc.example.services.GatewayService;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import timber.log.Timber;

public class WebRTCExampleApplication extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        installServiceProviderIfNeeded();
        initializeGatewayService();
        initializeTimber();
    }

    private void initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Contract(pure = true)
    public static Application getApplication() {
        return application;
    }

    @Nullable
    public static Context getAppContext() {
        return application != null ? application.getApplicationContext() : null;
    }

    private static void installServiceProviderIfNeeded() {
        try {
            ProviderInstaller.installIfNeeded(getAppContext());

            final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            final SSLEngine engine = sslContext.createSSLEngine();
        } catch (Throwable e) {
            //
        }
    }

    private void initializeGatewayService() {
        final Intent intentFocusService = new Intent(getAppContext(), GatewayService.class);
        getAppContext().startService(intentFocusService);
    }
}
