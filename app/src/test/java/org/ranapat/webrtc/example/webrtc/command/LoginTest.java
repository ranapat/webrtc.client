package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Peer;
import org.ranapat.webrtc.example.webrtc.Status;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LoginTest {

    @Test
    public void shouldExecuteCase1() throws JSONException {
        final Peer peer = mock(Peer.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject payload = new JSONObject("{\"mode\":\"accepted\"}");

        final Login login = new Login();
        login.execute(peer, payload, clientListener);

        verify(clientListener, times(1)).onSocketStatus(Status.CONNECTED, "");
    }

    @Test
    public void shouldExecuteCase2() throws JSONException {
        final Peer peer = mock(Peer.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject payload = new JSONObject("{\"mode\":\"something else\"}");

        final Login login = new Login();
        login.execute(peer, payload, clientListener);

        verify(clientListener, times(1)).onSocketStatus(Status.FAILED, "");
    }

    @Test
    public void shouldExecuteCase3() throws JSONException {
        final Peer peer = mock(Peer.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject payload = new JSONObject("{\"_mode\":\"something else\"}");

        final Login login = new Login();
        login.execute(peer, payload, clientListener);

        verify(clientListener, times(1)).onSocketStatus(Status.FAILED, "");
    }

}