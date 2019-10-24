package org.ranapat.webrtc.example.api.map;

import org.ranapat.webrtc.example.data.entity.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StreamParser implements CollectionParseable<Stream> {
    @Override
    public List<Stream> parse(final JSONArray jsonArray) throws JSONException {
        final List<Stream> streams = new ArrayList<>();

        final int length = jsonArray.length();
        for (int i = 0; i < length; ++i) {
            final JSONObject stream = jsonArray.getJSONObject(i);

            streams.add(new Stream(
                    stream.getString("id"),
                    stream.getString("name")
            ));
        }

        return streams;
    }
}
