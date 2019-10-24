package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Peer;

import org.json.JSONObject;

public class Terminate implements Command {
    @Override
    public void execute(final Peer peer, final JSONObject payload, final ClientListener clientListener) {
        clientListener.onTerminate(peer != null ? peer.id : "undefined");
    }
}
