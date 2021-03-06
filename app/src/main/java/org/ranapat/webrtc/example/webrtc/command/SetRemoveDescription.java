package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Peer;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.SessionDescription;

public final class SetRemoveDescription implements Command {
    @Override
    public void execute(final Peer peer, final JSONObject payload, final ClientListener clientListener) {
        try {
            final SessionDescription sessionDescription = new SessionDescription(
                    SessionDescription.Type.fromCanonicalForm(payload.getString("type")),
                    payload.getString("sdp")
            );
            peer.getPeerConnection().setRemoteDescription(peer, sessionDescription);
        } catch (final JSONException e) {
            clientListener.onThrowable(e);
        }
    }
}
