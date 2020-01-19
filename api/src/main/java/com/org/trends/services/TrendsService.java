package com.org.trends.services;

import com.org.trends.util.Connection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TrendsService {

    private Connection connection;

    @Autowired
    public TrendsService(Connection connection) {
        this.connection = connection;
    }

    public JSONArray search(String term) throws IOException {
        JSONObject result = new JSONObject(connection.search(term));

        result = result.getJSONObject("aggregations");
        result = result.getJSONObject("genres");
        JSONArray hits = result.getJSONArray("buckets");

        return customize(hits);
    }

    private JSONArray customize(JSONArray hits) {
        JSONArray newJson = new JSONArray();

        hits.forEach(h -> {
            String key = ((JSONObject) h).getString("key");
            Integer value = ((JSONObject) h).getInt("doc_count");

            Map<String, Object> item = new HashMap<>();
            item.put("name", "#" + key);
            item.put("value", value);

            newJson.put(item);
        });

        return newJson;
    }
}
