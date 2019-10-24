package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Peer;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddIceCandidateTest {

    @Test
    public void shouldExecuteCase1() throws JSONException {
        final Peer peer = mock(Peer.class);
        final PeerConnection peerConnection = mock(PeerConnection.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject payload = new JSONObject("{\"id\":\"id\",\"label\":1,\"candidate\":\"candidate\"}");

        when(peer.getPeerConnection()).thenReturn(peerConnection);
        when(peerConnection.getRemoteDescription()).thenReturn(mock(SessionDescription.class));

        final AddIceCandidate addIceCandidate = new AddIceCandidate();
        addIceCandidate.execute(peer, payload, clientListener);

        verify(clientListener, times(0)).onThrowable((Throwable) any());
        verify(peerConnection, times(1)).getRemoteDescription();
        verify(peerConnection, times(1)).addIceCandidate((IceCandidate) any());
    }

    @Test
    public void shouldExecuteCase2() throws JSONException {
        final Peer peer = mock(Peer.class);
        final PeerConnection peerConnection = mock(PeerConnection.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject payload = new JSONObject("{\"id\":\"id\",\"label\":1,\"candidate\":\"candidate\"}");

        when(peer.getPeerConnection()).thenReturn(peerConnection);
        when(peerConnection.getRemoteDescription()).thenReturn(null);

        final AddIceCandidate addIceCandidate = new AddIceCandidate();
        addIceCandidate.execute(peer, payload, clientListener);

        verify(clientListener, times(0)).onThrowable((Throwable) any());
        verify(peerConnection, times(1)).getRemoteDescription();
        verify(peerConnection, times(0)).addIceCandidate((IceCandidate) any());
    }

    @Test
    public void shouldExecuteCase3() throws JSONException {
        final Peer peer = mock(Peer.class);
        final PeerConnection peerConnection = mock(PeerConnection.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject payload = new JSONObject("{}");

        when(peer.getPeerConnection()).thenReturn(peerConnection);
        when(peerConnection.getRemoteDescription()).thenReturn(mock(SessionDescription.class));

        final AddIceCandidate addIceCandidate = new AddIceCandidate();
        addIceCandidate.execute(peer, payload, clientListener);

        verify(clientListener, times(1)).onThrowable((Throwable) any());
        verify(peerConnection, times(1)).getRemoteDescription();
        verify(peerConnection, times(0)).addIceCandidate((IceCandidate) any());
    }

    @Test
    public void shouldExecuteCase4() throws JSONException {
        final Peer peer = mock(Peer.class);
        final PeerConnection peerConnection = mock(PeerConnection.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject payload = new JSONObject("{}");

        when(peer.getPeerConnection()).thenReturn(peerConnection);
        when(peerConnection.getRemoteDescription()).thenReturn(null);

        final AddIceCandidate addIceCandidate = new AddIceCandidate();
        addIceCandidate.execute(peer, payload, clientListener);

        verify(clientListener, times(0)).onThrowable((Throwable) any());
        verify(peerConnection, times(1)).getRemoteDescription();
        verify(peerConnection, times(0)).addIceCandidate((IceCandidate) any());
    }

}