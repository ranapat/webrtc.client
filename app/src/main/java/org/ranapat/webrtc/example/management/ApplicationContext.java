package org.ranapat.webrtc.example.management;

import android.content.Context;

import org.ranapat.webrtc.example.WebRTCExampleApplication;
import org.ranapat.webrtc.example.dependencies.StaticallyInstantiable;

@StaticallyInstantiable
public abstract class ApplicationContext extends Context {
    private ApplicationContext() {}

    public static Context getInstance() {
        return WebRTCExampleApplication.getAppContext();
    }
}
