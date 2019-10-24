package org.ranapat.webrtc.example.webrtc;

import org.ranapat.webrtc.example.webrtc.command.Commands;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoTrack;

public final class Peer implements SdpObserver, PeerConnection.Observer {
    public final String id;
    private final PeerListener peerListener;

    private PeerConnection peerConnection;

    public Peer(
            final String id,
            final PeerListener peerListener
    ) {
        this.id = id;
        this.peerListener = peerListener;
    }

    public void setPeerConnection(final PeerConnection peerConnection) {
        this.peerConnection = peerConnection;
    }

    public PeerConnection getPeerConnection() {
        return peerConnection;
    }

    @Override
    public void onCreateSuccess(final SessionDescription sessionDescription) {
        try {
            peerConnection.setLocalDescription(this, sessionDescription);

            final JSONObject payload = new JSONObject();
            payload.put("type", sessionDescription.type.canonicalForm());
            payload.put("sdp", sessionDescription.description);
            peerListener.onMessage(id, sessionDescription.type.canonicalForm(), payload);
        } catch (final JSONException e) {
            peerListener.onThrowable(e);
        }
    }

    @Override
    public void onIceConnectionChange(final PeerConnection.IceConnectionState iceConnectionState) {
        if (iceConnectionState == PeerConnection.IceConnectionState.CONNECTED) {
            peerListener.onIceConnected(id);
        } else if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED) {
            peerListener.onRemovePeer(id);
        } else if (iceConnectionState == PeerConnection.IceConnectionState.CLOSED) {
            peerListener.onIceClosed(id);
        }
    }

    @Override
    public void onIceCandidate(final IceCandidate iceCandidate) {
        try {
            final JSONObject payload = new JSONObject();
            payload.put("label", iceCandidate.sdpMLineIndex);
            payload.put("id", iceCandidate.sdpMid);
            payload.put("candidate", iceCandidate.sdp);
            peerListener.onMessage(id, Commands.ADD_ICE_CANDIDATE, payload);
        } catch (final JSONException e) {
            peerListener.onThrowable(e);
        }
    }

    @Override
    public void onAddStream(final MediaStream mediaStream) {
        for (final AudioTrack audioTrack : mediaStream.audioTracks) {
            audioTrack.setEnabled(true);
        }

        if (mediaStream.videoTracks.size() > 0) {
            final VideoTrack remoteVideoTrack = mediaStream.videoTracks.get(0);

            peerListener.onRemoteVideoTrack(remoteVideoTrack);
        }
    }

    @Override
    public void onRemoveStream(final MediaStream mediaStream) {
        peerListener.onRemovePeer(id);
    }

    @Override
    public void onSetSuccess() {
        //
    }

    @Override
    public void onCreateFailure(final String s) {
        //
    }

    @Override
    public void onSetFailure(final String s) {
        //
    }

    @Override
    public void onSignalingChange(final PeerConnection.SignalingState signalingState) {
        //
    }

    @Override
    public void onIceGatheringChange(final PeerConnection.IceGatheringState iceGatheringState) {
        //
    }

    @Override
    public void onDataChannel(final DataChannel dataChannel) {
        //
    }

    @Override
    public void onRenegotiationNeeded() {
        //
    }

    @Override
    public void onAddTrack(final RtpReceiver var1, final MediaStream[] var2) {
        //
    }

    @Override
    public void onIceCandidatesRemoved(final IceCandidate[] var1) {
        //
    }

    public void onIceConnectionReceivingChange(final boolean var1) {
        //
    }

}
