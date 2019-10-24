package org.ranapat.webrtc.example.data.entity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StreamTest {

    @Test
    public void shouldPopulateFields() {
        final Stream stream = new Stream(
                "id", "name"
        );
        assertThat(stream.id, is(equalTo("id")));
        assertThat(stream.name, is(equalTo("name")));
    }

}