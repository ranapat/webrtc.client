package org.ranapat.webrtc.example.api.rao;

import org.ranapat.webrtc.example.api.exceptions.RequestFailedException;
import org.ranapat.webrtc.example.data.entity.Stream;
import org.ranapat.webrtc.example.dependencies.InstanceFactory;
import org.ranapat.webrtc.example.management.NetworkManager;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import gherkin.lexer.Encoding;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class StreamRaoTest {

    @Before
    public void beforeTest() {
        final NetworkManager networkManager = mock(NetworkManager.class);

        InstanceFactory.set(NetworkManager.class, networkManager);
    }

    @After
    public void afterTest() {
        InstanceFactory.remove(NetworkManager.class);
    }

    @Test
    public void shouldReturnStreams() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setBody(
                                new String(Files.readAllBytes(Paths.get(
                                        getClass().getClassLoader().getResource("mocks/streams/Streams.json").getPath()
                                )), Encoding.DEFAULT_ENCODING)
                                        .replaceAll("https?://[^/]+/", baseUrl.toString())
                        )
        );

        final StreamRao streamsRao = new StreamRao();
        final List<Stream> streams = streamsRao.fetchAll(baseUrl.toString());

        assertThat(server.takeRequest().getRequestUrl().toString(), is(equalTo(baseUrl.toString())));

        assertThat(streams.size(), is(equalTo(1)));
        assertThat(streams.get(0).id, is(equalTo("EFS_TS4geh5GeVS5AAAM")));
        assertThat(streams.get(0).name, is(equalTo("Guest")));

        server.shutdown();
    }

    @Test(expected = RequestFailedException.class)
    public void shouldThrowOnBadResponseOnFirstResponse() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody(
                                new String(Files.readAllBytes(Paths.get(
                                        getClass().getClassLoader().getResource("mocks/streams/Streams.json").getPath()
                                )), Encoding.DEFAULT_ENCODING)
                                        .replaceAll("https?://[^/]+/", baseUrl.toString())
                        )
        );

        final StreamRao streamsRao = new StreamRao();
        final List<Stream> streams = streamsRao.fetchAll(baseUrl.toString());

        server.shutdown();
    }

    @Test(expected = JSONException.class)
    public void shouldThrowOnBadJsonOnFirstResponse() throws Exception {
        final MockWebServer server = new MockWebServer();

        final HttpUrl baseUrl = server.url("");

        server.enqueue(
                new MockResponse()
                        .setBody(
                                ""
                        )
        );

        final StreamRao streamsRao = new StreamRao();
        final List<Stream> streams = streamsRao.fetchAll(baseUrl.toString());

        server.shutdown();
    }

}