package org.ranapat.webrtc.example.api.rao;

import org.ranapat.webrtc.example.api.map.StreamParser;
import org.ranapat.webrtc.example.data.entity.Stream;

import org.json.JSONArray;

import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class StreamRao extends BaseRao {
    public StreamRao() {
        super();
    }

    public List<Stream> fetchAll(final String streamsUrl) throws Exception {
        try {
            return doFetchAll(streamsUrl);
        } catch (final Exception exception) {
            logException(exception, "StreamRao::fetchAll");

            throw exception;
        }
    }

    private List<Stream> doFetchAll(final String streamsUrl) throws Exception {
        final Request request = new Request.Builder()
                .url(streamsUrl)
                .build();

        final Response response = getResponse(request);
        final String responseString = getResponseString(response);
        final JSONArray json = new JSONArray(responseString);
        final StreamParser streamsParser = new StreamParser();

        return streamsParser.parse(json);
    }
}
