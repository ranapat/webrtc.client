package org.ranapat.webrtc.example.api.map;

import org.json.JSONArray;
import org.json.JSONException;
import org.ranapat.webrtc.example.data.entity.DataEntity;

import java.util.List;

public interface CollectionParseable<T extends DataEntity> {
    List<T> parse(final JSONArray jsonArray) throws JSONException;
}