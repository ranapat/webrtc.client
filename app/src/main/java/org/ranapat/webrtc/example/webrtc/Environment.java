package org.ranapat.webrtc.example.webrtc;

import android.content.Context;
import android.media.AudioManager;

import org.ranapat.webrtc.example.Settings;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;
import org.ranapat.webrtc.example.management.ApplicationContext;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CapturerObserver;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

public final class Environment {
    private static final String LOCAL_MEDIA_STREAM_LABEL = "ARDAMS";
    private static final String AUDIO_TRACK_ID = "ARDAMSa0";
    private static final String VIDEO_TRACK_ID = "ARDAMSv0";

    private WeakReference<Context> context;

    private boolean initialized;
    private PeerConnectionFactory peerConnectionFactory;

    private EglBase eglBase;
    private VideoCapturer localVideoCapturer;
    private AudioSource localAudioSource;
    private AudioTrack localAudioTrack;
    private VideoSource localVideoSource;
    private CapturerObserver capturerObserver;
    private SurfaceTextureHelper surfaceTextureHelper;
    private VideoTrack localVideoTrack;
    private SurfaceViewRenderer localSurfaceViewRenderer;
    private SurfaceViewRenderer remoteSurfaceViewRenderer;
    private MediaStream localMediaStream;

    public static final LinkedList<PeerConnection.IceServer> iceServers;
    public static final MediaConstraints mediaConstraints;

    static {
        iceServers = new LinkedList<>();
        mediaConstraints = new MediaConstraints();

        if (Settings.useIceServers) {
            final PeerConnection.IceServer.Builder iceServerBuilder = PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302");
            iceServerBuilder.setTlsCertPolicy(PeerConnection.TlsCertPolicy.TLS_CERT_POLICY_INSECURE_NO_CHECK);
            PeerConnection.IceServer iceServer =  iceServerBuilder.createIceServer();
            iceServers.add(iceServer);
        }

        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        mediaConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
    }

    public Environment(
            final Context context
    ) {
        this.context = new WeakReference<>(context);
    }

    public Environment() {
        this(
                InstanceFactory.get(ApplicationContext.class)
        );
    }

    public void initialize() {
        if (!initialized) {
            initialized = true;

            final AudioManager audioManager = (AudioManager) context.get().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            audioManager.setSpeakerphoneOn(true);

            final PeerConnectionFactory.InitializationOptions initializationOptions = PeerConnectionFactory.InitializationOptions
                    .builder(context.get())
                    .setEnableInternalTracer(true)
                    .createInitializationOptions();
            PeerConnectionFactory.initialize(initializationOptions);

            eglBase = EglBase.create();

            final PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
            final VideoEncoderFactory videoEncoderFactory = new DefaultVideoEncoderFactory(eglBase.getEglBaseContext(), true, true);
            final VideoDecoderFactory videoDecoderFactory = new DefaultVideoDecoderFactory(eglBase.getEglBaseContext());
            final PeerConnectionFactory.Builder builder = PeerConnectionFactory.builder()
                    .setVideoEncoderFactory(videoEncoderFactory)
                    .setVideoDecoderFactory(videoDecoderFactory)
                    .setOptions(options);
            peerConnectionFactory = builder.createPeerConnectionFactory();

            localVideoCapturer = createVideoCapturer();
            localAudioSource = peerConnectionFactory.createAudioSource(mediaConstraints);
            localAudioTrack = peerConnectionFactory.createAudioTrack(AUDIO_TRACK_ID, localAudioSource);
            localAudioTrack.setEnabled(true);

            localVideoSource = peerConnectionFactory.createVideoSource(false);
            capturerObserver = localVideoSource.getCapturerObserver();
            surfaceTextureHelper = SurfaceTextureHelper.create("VideoCapturerThread", eglBase.getEglBaseContext());
            localVideoCapturer.initialize(surfaceTextureHelper, context.get(), capturerObserver);

            localVideoTrack = peerConnectionFactory.createVideoTrack(VIDEO_TRACK_ID, localVideoSource);
            localVideoTrack.setEnabled(true);

            localMediaStream = peerConnectionFactory.createLocalMediaStream(LOCAL_MEDIA_STREAM_LABEL);
            localMediaStream.addTrack(localAudioTrack);
            localMediaStream.addTrack(localVideoTrack);
        }
    }

    public void startCapture() {
        if (isInitialized()) {
            localVideoCapturer.startCapture(320, 200, 30);
        }
    }

    public void stopCapture() {
        if (isInitialized()) {
            try {
                localVideoCapturer.stopCapture();
            } catch (final InterruptedException e) {
                //
            }
        }
    }

    public void dispose() {
        if (isInitialized()) {
            initialized = false;

            localMediaStream = null;

            localAudioSource.dispose();
            localAudioSource = null;

            localVideoSource.dispose();
            localVideoSource = null;

            try {
                localAudioTrack.dispose();
            } catch (final IllegalStateException e) {
                e.printStackTrace();
            }
            localAudioTrack = null;

            try {
                localVideoTrack.dispose();
            } catch (final IllegalStateException e) {
                e.printStackTrace();
            }
            localVideoTrack = null;

            eglBase.release();
            eglBase = null;

            if (localSurfaceViewRenderer != null) {
                localSurfaceViewRenderer.clearImage();
                localSurfaceViewRenderer.release();
                localSurfaceViewRenderer = null;
            }
            if (remoteSurfaceViewRenderer != null) {
                remoteSurfaceViewRenderer.clearImage();
                remoteSurfaceViewRenderer.release();
                remoteSurfaceViewRenderer = null;
            }

            try {
                localVideoCapturer.stopCapture();
            } catch (final InterruptedException e) {
                //
            }
            localVideoCapturer.dispose();
            localVideoCapturer = null;

            peerConnectionFactory.dispose();
            peerConnectionFactory = null;

            PeerConnectionFactory.stopInternalTracingCapture();
            PeerConnectionFactory.shutdownInternalTracer();
        }
    }

    public void initializeLocalSurfaceViewRenderer(final SurfaceViewRenderer view) {
        if (localSurfaceViewRenderer == null) {
            localSurfaceViewRenderer = view;

            localSurfaceViewRenderer.init(eglBase.getEglBaseContext(), null);
            localSurfaceViewRenderer.setZOrderMediaOverlay(true);
            localSurfaceViewRenderer.setMirror(true);

            localVideoTrack.addSink(localSurfaceViewRenderer);
        }
    }

    public void initializeRemoteSurfaceViewRenderer(final SurfaceViewRenderer view) {
        if (remoteSurfaceViewRenderer == null) {
            remoteSurfaceViewRenderer = view;

            remoteSurfaceViewRenderer.init(eglBase.getEglBaseContext(), null);
            remoteSurfaceViewRenderer.setZOrderMediaOverlay(true);
            remoteSurfaceViewRenderer.setMirror(true);
        }
    }

    public PeerConnection peerConnection(final PeerConnection.Observer observer) {
        if (isInitialized()) {
            final PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
            rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
            rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
            rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
            rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
            rtcConfig.keyType = PeerConnection.KeyType.ECDSA;
            final PeerConnection peerConnection = peerConnectionFactory.createPeerConnection(
                    rtcConfig,
                    observer
            );
            peerConnection.addStream(localMediaStream);

            return peerConnection;
        } else {
            return null;
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public SurfaceViewRenderer getRemoteSurfaceViewRenderer() {
        return remoteSurfaceViewRenderer;
    }

    private VideoCapturer createVideoCapturer() {
        CameraEnumerator cameraEnumerator;
        if (Camera2Enumerator.isSupported(context.get())) {
            cameraEnumerator = new Camera2Enumerator(context.get());
        } else {
            cameraEnumerator = new Camera1Enumerator(false);
        }

        final String[] deviceNames = cameraEnumerator.getDeviceNames();

        for (final String deviceName : deviceNames) {
            if (cameraEnumerator.isFrontFacing(deviceName)) {
                final VideoCapturer videoCapturer = cameraEnumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        for (final String deviceName2 : deviceNames) {
            if (!cameraEnumerator.isFrontFacing(deviceName2)) {
                final VideoCapturer videoCapturer2 = cameraEnumerator.createCapturer(deviceName2, null);
                if (videoCapturer2 != null) {
                    return videoCapturer2;
                }
            }
        }

        return null;
    }

}
