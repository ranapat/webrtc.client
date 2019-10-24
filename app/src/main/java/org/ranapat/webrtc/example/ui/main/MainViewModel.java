package org.ranapat.webrtc.example.ui.main;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.ranapat.webrtc.example.R;
import org.ranapat.webrtc.example.data.entity.Stream;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;
import org.ranapat.webrtc.example.management.ApplicationContext;
import org.ranapat.webrtc.example.management.NetworkManager;
import org.ranapat.webrtc.example.observable.stream.StreamObservable;
import org.ranapat.webrtc.example.ui.BaseViewModel;
import org.ranapat.webrtc.example.ui.call.CallActivity;
import org.ranapat.webrtc.example.ui.publishable.ParameterizedMessage;
import org.ranapat.webrtc.example.webrtc.Gateway;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static org.ranapat.webrtc.example.ui.common.States.CLEAN_REDIRECT;
import static org.ranapat.webrtc.example.ui.common.States.ERROR;
import static org.ranapat.webrtc.example.ui.common.States.LOADING;
import static org.ranapat.webrtc.example.ui.common.States.READY;

public class MainViewModel extends BaseViewModel {
    final public PublishSubject<String> state;
    final public PublishSubject<ParameterizedMessage> message;
    final public PublishSubject<List<Stream>> streams;
    final public PublishSubject<Class<? extends AppCompatActivity>> next;
    final public PublishSubject<Stream> selectedStream;

    final private StreamObservable streamObservable;

    final private WeakReference<Context> context;

    final private Gateway gateway;
    private List<Stream> currentStreams;

    public MainViewModel(
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
        streams = PublishSubject.create();
        next = PublishSubject.create();
        selectedStream = PublishSubject.create();

        currentStreams = new ArrayList<>();
    }

    public MainViewModel() {
        this(
                InstanceFactory.get(NetworkManager.class),
                InstanceFactory.get(StreamObservable.class),
                InstanceFactory.get(Gateway.class),
                InstanceFactory.get(ApplicationContext.class)
        );
    }

    public void initialize() {
        state.onNext(LOADING);

        subscription(streamObservable
                .fetchAll()
                .subscribe(new Consumer<List<Stream>>() {
                    @Override
                    public void accept(final List<Stream> _streams) {
                        currentStreams = _streams;
                        initializeGateway();

                        streams.onNext(_streams);
                        state.onNext(READY);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) {
                        message.onNext(new ParameterizedMessage(R.string.main_error_loading));
                        state.onNext(ERROR);
                    }
                })
        );
    }

    public void call(final Stream stream) {
        selectedStream.onNext(stream);
        next.onNext(CallActivity.class);
        state.onNext(CLEAN_REDIRECT);
    }

    @Override
    protected void triggerNetworkStatus(final Boolean isOnline) {
        //
    }

    private void initializeGateway() {
        subscription(gateway
                .join
                .subscribe(new Consumer<Stream>() {
                    @Override
                    public void accept(final Stream stream) {
                        if (!stream.name.isEmpty()) {
                            for (final Stream _stream : currentStreams) {
                                if (_stream.id.equals(stream.id)) {
                                    return;
                                }
                            }

                            currentStreams.add(stream);
                            streams.onNext(currentStreams);
                        }
                    }
                })
        );
        subscription(gateway
                .leave
                .subscribe(new Consumer<Stream>() {
                    @Override
                    public void accept(final Stream stream) {
                        int index = 0;
                        for (final Stream _stream : currentStreams) {
                            if (_stream.id.equals(stream.id)) {
                                currentStreams.remove(index);
                                streams.onNext(currentStreams);

                                return;
                            }
                            ++index;
                        }
                    }
                })
        );
    }

}
