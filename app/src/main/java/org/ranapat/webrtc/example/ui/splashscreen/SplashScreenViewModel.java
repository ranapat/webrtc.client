package org.ranapat.webrtc.example.ui.splashscreen;

import android.app.Activity;
import android.content.Context;

import org.ranapat.webrtc.example.R;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;
import org.ranapat.webrtc.example.management.ApplicationContext;
import org.ranapat.webrtc.example.management.NetworkManager;
import org.ranapat.webrtc.example.ui.BaseViewModel;
import org.ranapat.webrtc.example.ui.main.MainActivity;
import org.ranapat.webrtc.example.ui.publishable.ParameterizedMessage;

import java.lang.ref.WeakReference;

import io.reactivex.subjects.PublishSubject;

import static org.ranapat.webrtc.example.ui.common.States.COMPLETE;
import static org.ranapat.webrtc.example.ui.common.States.ERROR;
import static org.ranapat.webrtc.example.ui.common.States.LOADING;

public class SplashScreenViewModel extends BaseViewModel {
    final public PublishSubject<ParameterizedMessage> message;
    final public PublishSubject<String> state;
    final public PublishSubject<Class<? extends Activity>> next;

    final private WeakReference<Context> context;

    public SplashScreenViewModel(
            final NetworkManager networkManager,
            final Context context
    ) {
        super(networkManager);

        this.context = new WeakReference<>(context);

        message = PublishSubject.create();
        state = PublishSubject.create();
        next = PublishSubject.create();
    }

    public SplashScreenViewModel() {
        this(
                InstanceFactory.get(NetworkManager.class),
                InstanceFactory.get(ApplicationContext.class)
        );
    }

    public void initialize() {
        state.onNext(LOADING);

        if (isOnline()) {
            next.onNext(MainActivity.class);
            state.onNext(COMPLETE);
        } else {
            message.onNext(new ParameterizedMessage(R.string.no_network_connection_available));
            state.onNext(ERROR);
        }
    }

    @Override
    protected void triggerNetworkStatus(final Boolean isOnline) {
        if (isOnline) {
            initialize();
        }
    }
}
