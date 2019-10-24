package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Environment;
import org.ranapat.webrtc.example.webrtc.Peer;

import org.json.JSONObject;

public final class CreateOffer implements Command {
    @Override
    public void execute(final Peer peer, final JSONObject payload, final ClientListener clientListener) {
        peer.getPeerConnection().createOffer(peer, Environment.mediaConstraints);

        clientListener.onInitializingCall(peer.id);
    }
}
