package org.ranapat.webrtc.example.webrtc;

import org.json.JSONObject;
import org.webrtc.VideoTrack;

public interface PeerListener extends WebRtcListener {
    void onIceConnected(final String id);
    void onIceClosed(final String id);
    void onRemovePeer(final String id);
    void onMessage(final String peerId, final String type, final JSONObject payload);
    void onRemoteVideoTrack(final VideoTrack videoTrack);
}
