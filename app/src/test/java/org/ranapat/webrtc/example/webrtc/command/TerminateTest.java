package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Peer;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TerminateTest {

    @Test
    public void shouldExecuteCase1() {
        final Peer peer = mock(Peer.class);
        final ClientListener clientListener = mock(ClientListener.class);

        final Terminate terminate = new Terminate();
        terminate.execute(peer, null, clientListener);

        verify(clientListener, times(1)).onTerminate(peer.id);
    }

    @Test
    public void shouldExecuteCase2() {
        final ClientListener clientListener = mock(ClientListener.class);

        final Terminate terminate = new Terminate();
        terminate.execute(null, null, clientListener);

        verify(clientListener, times(1)).onTerminate("undefined");
    }

}