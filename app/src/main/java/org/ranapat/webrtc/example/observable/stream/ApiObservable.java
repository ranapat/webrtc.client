package org.ranapat.webrtc.example.observable.stream;

import org.ranapat.webrtc.example.api.rao.StreamRao;
import org.ranapat.webrtc.example.data.entity.Stream;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

public class ApiObservable {
    final private StreamRao streamRao;

    public ApiObservable(final StreamRao streamRao) {
        this.streamRao = streamRao;
    }

    public ApiObservable() {
        this(
                InstanceFactory.get(StreamRao.class)
        );
    }

    public Maybe<List<Stream>> fetchAll(final String streamsUrl) {
        return Maybe.fromCallable(new Callable<List<Stream>>() {
            @Override
            public List<Stream> call() {
                try {
                    return streamRao.fetchAll(streamsUrl);
                } catch (final Exception e) {
                    return null;
                }
            }
        }).subscribeOn(Schedulers.io());
    }

}
