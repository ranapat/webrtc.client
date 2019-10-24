package org.ranapat.webrtc.example.webrtc.command;

import org.ranapat.webrtc.example.webrtc.ClientListener;
import org.ranapat.webrtc.example.webrtc.Peer;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UnlockTest {

    @Test
    public void shouldExecute() {
        final Peer peer = mock(Peer.class);
        final ClientListener clientListener = mock(ClientListener.class);

        final Unlock unlock = new Unlock();
        unlock.execute(peer, null, clientListener);

        verify(clientListener, times(1)).onUnlock(peer.id);
    }

}