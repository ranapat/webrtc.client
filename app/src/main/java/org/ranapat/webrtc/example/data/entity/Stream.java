package org.ranapat.webrtc.example.data.entity;

import java.io.Serializable;

public class Stream implements DataEntity, Serializable {
    public final String id;
    public final String name;

    public Stream(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
