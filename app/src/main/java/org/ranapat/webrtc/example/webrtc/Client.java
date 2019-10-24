package org.ranapat.webrtc.example.webrtc;

import org.json.JSONException;
import org.json.JSONObject;
import org.ranapat.webrtc.example.webrtc.command.Command;
import org.ranapat.webrtc.example.webrtc.command.Commands;
import org.webrtc.PeerConnection;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static org.ranapat.webrtc.example.webrtc.Gateway.CALL_MODE_ANSWER_RING;
import static org.ranapat.webrtc.example.webrtc.Gateway.CALL_MODE_INITIATE_RING;

public final class Client {
    private String id;
    private String name;

    private boolean connected;
    private boolean connecting;

    private final Environment environment;

    private final Socket socket;
    private final ClientListener listener;
    private final Map<String, Peer> peers;
    private final List<PeerConnection> peerConnectionsToDispose;
    private final Commands commandFactory;
    private final PeerListener peerListener;

    public Client(final String host, final ClientListener listener) throws URISyntaxException {
        this.listener = listener;

        environment = new Environment();
        peers = new HashMap<>();
        peerConnectionsToDispose = new ArrayList<>();
        commandFactory = new Commands();

        peerListener = new PeerListener() {
            @Override
            public void onIceConnected(final String id) {
                listener.onCallEstablished(id);
            }

            @Override
            public void onIceClosed(final String id) {
                listener.onTerminate(id);
            }

            @Override
            public void onRemovePeer(final String id) {
                removePeer(id);
            }

            @Override
            public void onMessage(final String peerId, final String type, final JSONObject payload) {
                sendMessage(peerId, type, payload);
            }

            @Override
            public void onThrowable(final Throwable throwable) {
                listener.onThrowable(throwable);
            }

            @Override
            public void onRemoteVideoTrack(VideoTrack videoTrack) {
                videoTrack.addSink(environment.getRemoteSurfaceViewRenderer());
            }
        };

        socket = IO.socket(host);
        socket.on("id", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                handleOnId((String) args[0]);
            }
        });
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                final JSONObject data = (JSONObject) args[0];
                final String from = data.optString("from");
                final String type = data.optString("type");
                final JSONObject payload = data.optJSONObject("payload");

                handleOnMessage(from, type, payload);
            }
        });
        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                handleOnDisconnect(args);
            }
        });
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //
            }
        });
        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //
            }
        });

        listener.onSocketStatus(Status.IDLE, "");
    }

    public void connect(final String name) {
        this.name = name;

        connected = false;
        connecting = true;

        listener.onSocketStatus(Status.CONNECTING, "");

        socket.open();
    }

    public void startCapture() {
        environment.startCapture();
    }

    public void stopCapture() {
        environment.stopCapture();
    }

    public void dispose() {
        for (final Map.Entry<String, Peer> entry : peers.entrySet()) {
            final PeerConnection peerConnection = entry.getValue().getPeerConnection();
            if (peerConnection != null) {
                final Thread disposeAsync = new Thread(new Runnable() {
                    public void run() {
                        peerConnection.dispose();
                    }
                });
                disposeAsync.run();
                try {
                    disposeAsync.join();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        peers.clear();

        for (final PeerConnection peerConnection : peerConnectionsToDispose) {
            if (peerConnection != null) {
                final Thread disposeAsync = new Thread(new Runnable() {
                    public void run() {
                        peerConnection.dispose();
                    }
                });
                disposeAsync.run();
                try {
                    disposeAsync.join();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        peerConnectionsToDispose.clear();

        environment.dispose();
    }

    public void initializeSurfaceViewRenderer(final SurfaceViewRenderer local, final SurfaceViewRenderer remote) {
        environment.initialize();

        environment.initializeLocalSurfaceViewRenderer(local);
        environment.initializeRemoteSurfaceViewRenderer(remote);
    }

    public String getId() {
        return id;
    }

    public void ring(final String id) {
        if (true || !id.equals(this.id)) {
            if (environment.isInitialized()) {
                sendMessage(id, Commands.RING);
            } else {
                listener.onThrowable(new EnvironmentNotInitializedException());
            }
        }
    }

    public void busy(final String id) {
        if (true || !id.equals(this.id)) {
            if (environment.isInitialized()) {
                sendMessage(id, Commands.BUSY);
            } else {
                listener.onThrowable(new EnvironmentNotInitializedException());
            }
        }
    }

    public void cancel(final String id) {
        if (true || !id.equals(this.id)) {
            if (environment.isInitialized()) {
                sendMessage(id, Commands.CANCEL);
            } else {
                listener.onThrowable(new EnvironmentNotInitializedException());
            }
        }
    }

    public void terminate(final String id) {
        if (true || !id.equals(this.id)) {
            if (environment.isInitialized()) {
                sendMessage(id, Commands.TERMINATE);
            } else {
                listener.onThrowable(new EnvironmentNotInitializedException());
            }
        }
    }

    public void unlock(final String id) {
        if (true || !id.equals(this.id)) {
            sendMessage(id, Commands.UNLOCK);
        }
    }

    public void createOffer(final String id) {
        if (true || !id.equals(this.id)) {
            if (environment.isInitialized()) {
                sendMessage(id, Commands.CREATE_OFFER);
                listener.onInitializingCall(id);
            } else {
                listener.onThrowable(new EnvironmentNotInitializedException());
            }
        }
    }

    public void end(final String id, final String currentCallMode, final boolean hasCallStarted) {
        if (currentCallMode.equals(CALL_MODE_ANSWER_RING)) {
            if (!hasCallStarted) {
                busy(id);
            } else {
                terminate(id);
            }
        } else if (currentCallMode.equals(CALL_MODE_INITIATE_RING)) {
            if (!hasCallStarted) {
                cancel(id);
            } else {
                terminate(id);
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isConnecting() {
        return connecting;
    }

    private void sendMessage(final String to, final String type) {
        sendMessage(to, type, null);
    }

    private void sendMessage(final String to, final String type, final JSONObject payload) {
        try {
            final JSONObject message = new JSONObject();
            message.put("to", to);
            message.put("type", type);
            message.put("payload", payload);

            socket.emit("message", message);
        } catch (final JSONException e) {
            listener.onThrowable(e);
        }
    }

    private void handleOnId(final String id) {
        this.id = id;

        connected = true;
        connecting = false;

        login();
    }

    private void handleOnDisconnect(final Object... args) {
        connected = false;
        connecting = true;

        listener.onSocketStatus(Status.DISCONNECTED, (String) args[0]);
        listener.onSocketStatus(Status.RECONNECTING, (String) args[0]);
    }

    private void handleOnMessage(final String from, final String type, final JSONObject payload) {
        Peer peer;
        if (!from.isEmpty()) {
            if (!peers.containsKey(from)) {
                peer = new Peer(from, peerListener);
                peer.setPeerConnection(environment.peerConnection(peer));
                if (peer.getPeerConnection() != null) {
                    peers.put(from, peer);
                }
            } else {
                peer = peers.get(from);
            }
        } else {
            peer = null;
        }

        final Command command = commandFactory.get(type);
        if (command != null) {
            command.execute(peer, payload, listener);
        }
    }

    private void login() {
        try {
            listener.onSocketStatus(Status.LOGGING_IN, "");

            final JSONObject message = new JSONObject();
            message.put("name", name);
            socket.emit(Commands.LOGIN, message);
        } catch (final JSONException e) {
            listener.onThrowable(e);
        }
    }

    private void removePeer(final String id) {
        final Peer peer = peers.get(id);
        final PeerConnection peerConnection = peer.getPeerConnection();
        listener.onRemovePeer(id);

        peerConnectionsToDispose.add(peerConnection);

        peers.remove(peer.id);
    }
}
