package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Peer;
import org.ranapat.webrtc.example.webrtc.Status;

import org.json.JSONObject;

public class Login implements Command {
    @Override
    public void execute(final Peer peer, final JSONObject payload, final ClientListener clientListener) {
        if (payload.optString("mode").equals("accepted")) {
            clientListener.onSocketStatus(Status.CONNECTED, "");
        } else {
            clientListener.onSocketStatus(Status.FAILED, "");
        }
    }
}
