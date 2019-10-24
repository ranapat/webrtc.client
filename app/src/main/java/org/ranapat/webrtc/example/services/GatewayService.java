package org.ranapat.webrtc.example.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import org.ranapat.webrtc.example.Settings;
import org.ranapat.webrtc.example.data.entity.Stream;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;
import org.ranapat.webrtc.example.observable.stream.StreamObservable;
import org.ranapat.webrtc.example.ui.call.CallActivity;
import org.ranapat.webrtc.example.ui.common.IntentParameters;
import org.ranapat.webrtc.example.webrtc.Gateway;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static org.ranapat.webrtc.example.webrtc.Gateway.CALL_MODE_ANSWER_RING;

public class GatewayService extends Service {
    final private StreamObservable streamObservable;
    final private CompositeDisposable compositeDisposable;

    private Gateway gateway;

    public GatewayService(final StreamObservable streamObservable) {
        this.streamObservable = streamObservable;

        compositeDisposable = new CompositeDisposable();
    }

    public GatewayService() {
        this(
                InstanceFactory.get(StreamObservable.class)
        );
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        handleStartCommand();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(final Intent rootIntent) {
        compositeDisposable.clear();

        if (gateway.client != null) {
            gateway.client.dispose();
        }

        super.onTaskRemoved(rootIntent);
        this.stopSelf();
    }

    private void handleStartCommand() {
        if (!InstanceFactory.isSet(Gateway.class)) {
            gateway = new Gateway(Settings.gatewayApi);
            InstanceFactory.set(Gateway.class, gateway);
        }

        gateway.client.connect(android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL);

        handleEvents();
    }

    private void handleEvents() {
        compositeDisposable.add(gateway
                .ring
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(final String id) {
                        handleRing(id);
                    }
                })
        );
    }

    private void handleRing(final String id) {
        if (gateway.isCallOngoing()) {
            gateway.client.busy(id);
        } else {
            final AtomicReference<Stream> atomicStream = new AtomicReference<>();

            compositeDisposable.add(streamObservable
                    .fetchAll()
                    .flatMap(new Function<List<Stream>, MaybeSource<Stream>>() {
                        @Override
                        public MaybeSource<Stream> apply(final List<Stream> streams) {
                            for (final Stream stream : streams) {
                                if (stream.id.equals(id)) {
                                    return Maybe.just(stream);
                                }
                            }

                            return Maybe.empty();
                        }
                    })
                    .subscribe(new Consumer<Stream>() {
                        @Override
                        public void accept(final Stream stream) {
                            final Intent intent = new Intent(getApplicationContext(), CallActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(IntentParameters.STREAM, stream);
                            intent.putExtra(IntentParameters.CALL_MODE, CALL_MODE_ANSWER_RING);

                            startActivity(intent);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(final Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    })
            );
        }
    }
}
