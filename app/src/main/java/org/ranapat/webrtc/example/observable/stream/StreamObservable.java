package org.ranapat.webrtc.example.observable.stream;

import org.ranapat.webrtc.example.Settings;
import org.ranapat.webrtc.example.data.entity.Stream;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;

import java.util.List;

import io.reactivex.Maybe;

public class StreamObservable {
    final private ApiObservable apiObservable;

    public StreamObservable(
            final ApiObservable apiObservable
    ) {
        this.apiObservable = apiObservable;
    }

    public StreamObservable() {
        this(
                InstanceFactory.get(ApiObservable.class)
        );
    }

    public Maybe<List<Stream>> fetchAll() {
        return fetchAll(Settings.streamsApi);
    }

    private Maybe<List<Stream>> fetchAll(final String streamsUrl) {
        return apiObservable
                .fetchAll(streamsUrl);
    }

}
