package org.ranapat.webrtc.example.ui.call;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.ranapat.webrtc.example.R;
import org.ranapat.webrtc.example.data.entity.Stream;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;
import org.ranapat.webrtc.example.management.ApplicationContext;
import org.ranapat.webrtc.example.management.NetworkManager;
import org.ranapat.webrtc.example.observable.stream.StreamObservable;
import org.ranapat.webrtc.example.ui.BaseViewModel;
import org.ranapat.webrtc.example.ui.main.MainActivity;
import org.ranapat.webrtc.example.ui.publishable.ParameterizedMessage;
import org.ranapat.webrtc.example.webrtc.Gateway;
import org.ranapat.webrtc.example.webrtc.GatewayStatus;
import org.ranapat.webrtc.example.webrtc.Status;
import org.webrtc.SurfaceViewRenderer;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static org.ranapat.webrtc.example.ui.common.States.ERROR;
import static org.ranapat.webrtc.example.ui.common.States.LOADING;
import static org.ranapat.webrtc.example.webrtc.Gateway.CALL_MODE_ANSWER_RING;
import static org.ranapat.webrtc.example.webrtc.Gateway.CALL_MODE_INITIATE_RING;

public class CallViewModel extends BaseViewModel {
    public static final String NOT_AVAILABLE = "notAvailable";
    public static final String BUSY = "busy";
    public static final String CANCEL = "cancel";
    public static final String TERMINATE = "terminate";
    public static final String RINGING = "ringing";
    public static final String INITIALIZING_CALL = "initializingCall";
    public static final String CALL_ESTABLISHED = "callEstablished";

    final public PublishSubject<String> state;
    final public PublishSubject<ParameterizedMessage> message;
    final public PublishSubject<Stream> stream;
    final public PublishSubject<Class<? extends AppCompatActivity>> next;

    final private StreamObservable streamObservable;

    final private Gateway gateway;

    final private WeakReference<Context> context;

    private Stream currentStream;
    private String currentCallMode;

    public CallViewModel(
            final NetworkManager networkManager,
            final StreamObservable streamObservable,
            final Gateway gateway,
            final Context context
    ) {
        super(networkManager);

        this.streamObservable = streamObservable;

        this.gateway = gateway;

        this.context = new WeakReference<>(context);

        state = PublishSubject.create();
        message = PublishSubject.create();
        stream = PublishSubject.create();
        next = PublishSubject.create();
    }

    public CallViewModel() {
        this(
                InstanceFactory.get(NetworkManager.class),
                InstanceFactory.get(StreamObservable.class),
                InstanceFactory.get(Gateway.class),
                InstanceFactory.get(ApplicationContext.class)
        );
    }

    public void initialize(
            final Stream stream,
            final String callMode,
            final SurfaceViewRenderer local, final SurfaceViewRenderer remote
    ) {
        if (stream == null) {
            message.onNext(new ParameterizedMessage(R.string.call_error));
            state.onNext(ERROR);

            return;
        }

        gateway.client.initializeSurfaceViewRenderer(local, remote);
        initializeGateway();

        currentStream = stream;
        currentCallMode = callMode;

        state.onNext(LOADING);

        next.onNext(MainActivity.class);

        initiateCall();
    }

    public void pause() {
        if (gateway.hasCallStarted()) {
            gateway.client.stopCapture();
        }
    }

    public void resume() {
        if (gateway.hasCallStarted()) {
            gateway.client.startCapture();
        }
    }

    public void dispose() {
        terminateCall();

        gateway.setCallOngoing(false);
        gateway.client.dispose();
    }

    public void preCallAction() {
        if (currentCallMode.equals(CALL_MODE_INITIATE_RING)) {
            //
        } else if (currentCallMode.equals(CALL_MODE_ANSWER_RING)) {
            answer();
        } else {
            message.onNext(new ParameterizedMessage(R.string.call_error));
            state.onNext(ERROR);
        }
    }

    @Override
    protected void triggerNetworkStatus(final Boolean isOnline) {
        //
    }

    private void initializeGateway() {
        subscription(gateway
                .initializingCall
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(final String id) {
                        state.onNext(INITIALIZING_CALL);
                    }
                })
        );
        subscription(gateway
                .callEstablished
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(final String id) {
                        state.onNext(CALL_ESTABLISHED);

                        gateway.client.startCapture();
                        gateway.startCall();
                    }
                })
        );
        subscription(gateway
                .busy
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(final String id) {
                        state.onNext(BUSY);
                    }
                })
        );
        subscription(gateway
                .cancel
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(final String id) {
                        state.onNext(CANCEL);
                    }
                })
        );
        subscription(gateway
                .terminate
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(final String id) {
                        state.onNext(TERMINATE);
                    }
                })
        );
        subscription(gateway
                .socketStatus
                .subscribe(new Consumer<GatewayStatus>() {
                    @Override
                    public void accept(final GatewayStatus gatewayStatus) {
                        if (gatewayStatus.status.equals(Status.DISCONNECTED) && !gateway.hasCallStarted()) {
                            state.onNext(TERMINATE);
                        }
                    }
                })
        );
    }

    private void initiateCall() {
        gateway.setCallOngoing(true);

        locateStream();
    }

    private void locateStream() {
        subscription(streamObservable
                .fetchAll()
                .subscribe(new Consumer<List<Stream>>() {
                    @Override
                    public void accept(final List<Stream> streams) {
                        for (final Stream stream : streams) {
                            if (stream.name.equals(currentStream.name)) {
                                if (currentCallMode.equals(CALL_MODE_INITIATE_RING)) {
                                    ring();
                                } else if (currentCallMode.equals(CALL_MODE_ANSWER_RING)) {
                                    state.onNext(RINGING);
                                } else {
                                    message.onNext(new ParameterizedMessage(R.string.call_error));
                                    state.onNext(ERROR);
                                }

                                return;
                            }
                        }

                        state.onNext(NOT_AVAILABLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                        state.onNext(NOT_AVAILABLE);
                    }
                })
        );
    }

    private void ring() {
        gateway.client.ring(currentStream.id);
    }

    private void answer() {
        gateway.client.createOffer(currentStream.id);
    }

    private void terminateCall() {
        if (currentStream != null) {
            gateway.client.end(currentStream.id, currentCallMode, gateway.hasCallStarted());
        }
    }
}