package org.ranapat.webrtc.example.webrtc.command;

import org.json.JSONException;
import org.json.JSONObject;
import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Peer;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JoinTest {

    @Test
    public void shouldExecuteCase1() throws JSONException {
        final Peer peer = mock(Peer.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject jsonObject = new JSONObject("{}");

        final Join join = new Join();
        join.execute(peer, jsonObject, clientListener);

        verify(clientListener, times(1)).onJoin("", "", "");
    }

    @Test
    public void shouldExecuteCase2() throws JSONException {
        final Peer peer = mock(Peer.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject jsonObject = new JSONObject("{\"mode\":\"mode\",\"id\":\"id\",\"name\":\"name\"}");

        final Join join = new Join();
        join.execute(peer, jsonObject, clientListener);

        verify(clientListener, times(1)).onJoin("mode", "id", "name");
    }

}