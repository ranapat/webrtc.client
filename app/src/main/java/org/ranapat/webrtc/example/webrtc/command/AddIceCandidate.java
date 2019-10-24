package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Peer;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;

public class AddIceCandidate implements Command {
    @Override
    public void execute(final Peer peer, final JSONObject payload, final ClientListener clientListener) {
        try {
            if (peer.getPeerConnection().getRemoteDescription() != null) {
                final IceCandidate candidate = new IceCandidate(
                        payload.getString("id"),
                        payload.getInt("label"),
                        payload.getString("candidate")
                );
                peer.getPeerConnection().addIceCandidate(candidate);
            }
        } catch (final JSONException e) {
            clientListener.onThrowable(e);
        }
    }
}
