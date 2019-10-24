package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Environment;
import org.ranapat.webrtc.example.webrtc.Peer;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateAnswerTest {

    @Test
    public void shouldExecuteCase1() throws JSONException {
        final Peer peer = mock(Peer.class);
        final PeerConnection peerConnection = mock(PeerConnection.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject payload = new JSONObject("{\"type\":\"offer\",\"sdp\":\"sdp\"}");

        when(peer.getPeerConnection()).thenReturn(peerConnection);
        when(peerConnection.getRemoteDescription()).thenReturn(mock(SessionDescription.class));

        final CreateAnswer createAnswer = new CreateAnswer();
        createAnswer.execute(peer, payload, clientListener);

        verify(clientListener, times(0)).onThrowable((Throwable) any());
        verify(peerConnection, times(1)).setRemoteDescription((Peer) any(), (SessionDescription) any());
        verify(peerConnection, times(1)).createAnswer(peer, Environment.mediaConstraints);
    }

    @Test
    public void shouldExecuteCase2() throws JSONException {
        final Peer peer = mock(Peer.class);
        final PeerConnection peerConnection = mock(PeerConnection.class);
        final ClientListener clientListener = mock(ClientListener.class);
        final JSONObject payload = new JSONObject("{}");

        when(peer.getPeerConnection()).thenReturn(peerConnection);
        when(peerConnection.getRemoteDescription()).thenReturn(mock(SessionDescription.class));

        final CreateAnswer createAnswer = new CreateAnswer();
        createAnswer.execute(peer, payload, clientListener);

        verify(clientListener, times(1)).onThrowable((Throwable) any());
        verify(peerConnection, times(0)).setRemoteDescription((Peer) any(), (SessionDescription) any());
        verify(peerConnection, times(0)).createAnswer(peer, Environment.mediaConstraints);
    }

}