package org.ranapat.webrtc.example.webrtc;

public interface ClientListener extends WebRtcListener {
    void onSocketStatus(final String status, final String message);
    void onJoin(final String mode, final String id, final String name);
    void onLeave(final String id, final String name);
    void onRemovePeer(final String id);
    void onRing(final String id);
    void onInitializingCall(final String id);
    void onCallEstablished(final String id);
    void onBusy(final String id);
    void onCancel(final String id);
    void onTerminate(final String id);
    void onUnlock(final String id);
}
