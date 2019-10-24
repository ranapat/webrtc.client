package org.ranapat.webrtc.example.webrtc.command;

import org.json.JSONObject;
import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Peer;

public class Leave implements Command {
    @Override
    public void execute(final Peer peer, final JSONObject payload, final ClientListener clientListener) {
        clientListener.onLeave(
                payload.optString("id"),
                payload.optString("name")
        );
    }
}