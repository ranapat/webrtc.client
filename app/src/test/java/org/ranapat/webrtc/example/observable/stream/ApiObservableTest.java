package org.ranapat.webrtc.example.observable.stream;

import org.ranapat.webrtc.example.api.rao.StreamRao;
import org.ranapat.webrtc.example.data.entity.Stream;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;

import org.junit.Test;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApiObservableTest {

    @Test
    public void shouldHandleDefaultConstructor() throws Exception {
        final StreamRao streamRao = mock(StreamRao.class);

        InstanceFactory.set(StreamRao.class, streamRao);

        final ApiObservable apiObservable = new ApiObservable(
        );

        apiObservable.fetchAll("url").test().awaitTerminalEvent();

        verify(streamRao, times(1)).fetchAll("url");

        InstanceFactory.remove(StreamRao.class);
    }

    @Test
    public void shouldCallFetchOnce() throws Exception {
        final StreamRao streamRao = mock(StreamRao.class);
        final ApiObservable apiObservable = new ApiObservable(
                streamRao
        );

        apiObservable.fetchAll("url").test().awaitTerminalEvent();

        verify(streamRao, times(1)).fetchAll("url");
    }

    @Test
    public void shouldEmitOnce() throws Exception {
        final Stream stream = mock(Stream.class);
        final StreamRao streamRao = mock(StreamRao.class);
        when(streamRao.fetchAll("url")).thenReturn(
                asList(stream)
        );
        ApiObservable apiObservable = new ApiObservable(
                streamRao
        );

        final TestObserver<List<Stream>> testObserver = apiObservable.fetchAll("url").test();

        testObserver.awaitTerminalEvent();
        assertThat(testObserver.valueCount(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).size(), is(equalTo(1)));
        assertThat(testObserver.values().get(0).get(0), is(equalTo(stream)));
    }

    @Test
    public void shouldNotThrowOnFetch() throws Exception {
        final StreamRao streamRao = mock(StreamRao.class);
        when(streamRao.fetchAll("url")).thenThrow(Exception.class);
        final ApiObservable apiObservable = new ApiObservable(
                streamRao
        );

        final TestObserver<List<Stream>> testObserver = apiObservable.fetchAll("url").test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoValues();
    }
}