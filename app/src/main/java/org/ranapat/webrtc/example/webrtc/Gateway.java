package org.ranapat.webrtc.example.webrtc;

import org.ranapat.webrtc.example.data.entity.Stream;

import java.net.URISyntaxException;

import io.reactivex.subjects.PublishSubject;

public final class Gateway {
    public static final String CALL_MODE_INITIATE_RING = "initiateRing";
    public static final String CALL_MODE_ANSWER_RING = "answerRing";

    public final PublishSubject<String> ring;
    public final PublishSubject<String> initializingCall;
    public final PublishSubject<String> callEstablished;
    public final PublishSubject<String> busy;
    public final PublishSubject<String> cancel;
    public final PublishSubject<String> terminate;
    public final PublishSubject<String> unlock;
    public final PublishSubject<GatewayStatus> socketStatus;
    public final PublishSubject<Stream> join;
    public final PublishSubject<Stream> leave;
    public final PublishSubject<String> remove;
    public final PublishSubject<Throwable> throwable;

    public Client client;
    private final ClientListener listener;

    private boolean callOngoing;
    private boolean callStarted;

    public Gateway(final String gatewayUrl) {
        ring = PublishSubject.create();
        initializingCall = PublishSubject.create();
        callEstablished = PublishSubject.create();
        busy = PublishSubject.create();
        cancel = PublishSubject.create();
        terminate = PublishSubject.create();
        unlock = PublishSubject.create();
        socketStatus = PublishSubject.create();
        join = PublishSubject.create();
        leave = PublishSubject.create();
        remove = PublishSubject.create();
        throwable = PublishSubject.create();

        listener = new ClientListener() {
            @Override
            public void onSocketStatus(final String _status, final String message) {
                socketStatus.onNext(new GatewayStatus(_status, message));
            }

            @Override
            public void onJoin(final String mode, final String id, final String name) {
                join.onNext(new Stream(id, name));
            }

            @Override
            public void onLeave(final String id, final String name) {
                leave.onNext(new Stream(id, name));
            }

            @Override
            public void onRemovePeer(final String id) {
                remove.onNext(id);
            }

            @Override
            public void onRing(final String id) {
                ring.onNext(id);
            }

            @Override
            public void onInitializingCall(final String id) {
                initializingCall.onNext(id);
            }

            @Override
            public void onCallEstablished(final String id) {
                callEstablished.onNext(id);
            }

            @Override
            public void onBusy(final String id) {
                busy.onNext(id);
            }

            @Override
            public void onCancel(final String id) {
                cancel.onNext(id);
            }

            @Override
            public void onTerminate(final String id) {
                terminate.onNext(id);
            }

            @Override
            public void onUnlock(final String id) {
                unlock.onNext(id);
            }

            @Override
            public void onThrowable(final Throwable _throwable) {
                throwable.onNext(_throwable);
            }
        };

        try {
            client = new Client(gatewayUrl, listener);
        } catch (final URISyntaxException e) {
            //
        }
    }

    public void setCallOngoing(final boolean value) {
        callOngoing = value;
        callStarted = false;
    }

    public void startCall() {
        callStarted = true;
    }

    public boolean isCallOngoing() {
        return callOngoing;
    }

    public boolean hasCallStarted() {
        return callStarted;
    }
}
