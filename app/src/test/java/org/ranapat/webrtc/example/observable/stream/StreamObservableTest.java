package org.ranapat.webrtc.example.observable.stream;

import org.junit.Test;
import org.ranapat.webrtc.example.Settings;
import org.ranapat.webrtc.example.data.entity.Stream;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StreamObservableTest {

    @Test
    public void shouldHandleDefaultConstructor() {
        final ApiObservable apiObservable = mock(ApiObservable.class);

        InstanceFactory.set(ApiObservable.class, apiObservable);

        when(apiObservable.fetchAll(Settings.streamsApi)).thenReturn(Maybe.<List<Stream>>empty());

        final StreamObservable streamObservable = new StreamObservable(
        );

        final TestObserver<List<Stream>> testObserver = streamObservable.fetchAll().test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(0)));

        InstanceFactory.remove(ApiObservable.class);
    }

    @Test
    public void shouldNotThrowExceptionOnEmptyResults() {
        final ApiObservable apiObservable = mock(ApiObservable.class);

        when(apiObservable.fetchAll(Settings.streamsApi)).thenReturn(Maybe.<List<Stream>>empty());

        final StreamObservable streamObservable = new StreamObservable(
                apiObservable
        );

        final TestObserver<List<Stream>> testObserver = streamObservable.fetchAll().test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(0)));
    }

    @Test
    public void shouldEmitUpToDateDataFromApi() {
        final Stream stream = mock(Stream.class);
        final ApiObservable apiObservable = mock(ApiObservable.class);

        when(apiObservable.fetchAll(Settings.streamsApi)).thenReturn(Maybe.just(asList(stream)));

        final StreamObservable streamObservable = new StreamObservable(
                apiObservable
        );

        TestObserver<List<Stream>> testObserver = streamObservable.fetchAll().test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(stream)));
    }

}