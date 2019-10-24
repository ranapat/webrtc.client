package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Environment;
import org.ranapat.webrtc.example.webrtc.Peer;

import org.junit.Test;
import org.webrtc.PeerConnection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateOfferTest {

    @Test
    public void shouldExecute() {
        final Peer peer = mock(Peer.class);
        final PeerConnection peerConnection = mock(PeerConnection.class);
        final ClientListener clientListener = mock(ClientListener.class);

        when(peer.getPeerConnection()).thenReturn(peerConnection);

        final CreateOffer createOffer = new CreateOffer();
        createOffer.execute(peer, null, clientListener);

        verify(clientListener, times(1)).onInitializingCall(peer.id);
        verify(peerConnection, times(1)).createOffer(peer, Environment.mediaConstraints);
    }

}