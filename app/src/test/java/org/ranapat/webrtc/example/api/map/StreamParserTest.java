package org.ranapat.webrtc.example.api.map;

import org.ranapat.webrtc.example.data.entity.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import gherkin.lexer.Encoding;

import static org.junit.Assert.assertEquals;

public class StreamParserTest {

    @Test
    public void shouldGetFields() throws Exception {
        final StreamParser streamsParser = new StreamParser();
        final String data = new String(Files.readAllBytes(Paths.get(
                getClass().getClassLoader().getResource("mocks/streams/Streams.json").getPath()
        )), Encoding.DEFAULT_ENCODING);
        final JSONArray jsonArray = new JSONArray(data);
        final List<Stream> streams = streamsParser.parse(jsonArray);

        assertEquals(1, streams.size());
        assertEquals("EFS_TS4geh5GeVS5AAAM", streams.get(0).id);
        assertEquals("Guest", streams.get(0).name);
    }

    @Test
    public void shouldNotThrow() throws Exception {
        final StreamParser streamsParser = new StreamParser();
        final JSONArray jsonArray = new JSONArray("[]");
        final List<Stream> streams = streamsParser.parse(jsonArray);

        assertEquals(0, streams.size());
    }

    @Test(expected = JSONException.class)
    public void shouldThrowCase1() throws Exception {
        final StreamParser streamsParser = new StreamParser();
        final JSONArray jsonArray = new JSONArray("[{\"wrong-id\":\"value\",\"name-wrong\":\"value\"}]");
        final List<Stream> streams = streamsParser.parse(jsonArray);
    }

    @Test(expected = JSONException.class)
    public void shouldThrowCase2() throws Exception {
        final StreamParser streamsParser = new StreamParser();
        final JSONArray jsonArray = new JSONArray("[{\"name-wrong}]");
        final List<Stream> streams = streamsParser.parse(jsonArray);
    }

}