package org.ranapat.webrtc.example.ui;

import androidx.lifecycle.ViewModel;

import org.ranapat.webrtc.example.dependencies.InstanceFactory;
import org.ranapat.webrtc.example.management.NetworkManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseViewModel extends ViewModel {
    private final NetworkManager networkManager;
    private final CompositeDisposable compositeDisposable;
    private boolean networkOnline;

    public BaseViewModel(
            final NetworkManager networkManager
    ) {
        this.networkManager = networkManager;

        compositeDisposable = new CompositeDisposable();

        networkOnline = networkManager.isOnline();
    }

    public BaseViewModel() {
        this(
                InstanceFactory.get(NetworkManager.class)
        );
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
    }

    public void onNetworkStatus() {
        boolean isOnline = networkManager.isOnline();

        if (isOnline != networkOnline) {
            triggerNetworkStatus(isOnline);
        }

        networkOnline = isOnline;
    }

    protected abstract void triggerNetworkStatus(final Boolean isOnline);

    protected void subscription(final Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected boolean isOnline() {
        return networkManager.isOnline();
    }

    protected boolean isWifi() {
        return networkManager.isWifi();
    }

    protected boolean isMobile() {
        return networkManager.isMobile();
    }

}
