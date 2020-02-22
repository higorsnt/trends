package com.org.trends.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static String termNormalizer(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static JSONArray convertToJSON(String response) {
        JSONObject result = new JSONObject(response);

        result = result.getJSONObject("aggregations");
        result = result.getJSONObject("sterms#top_hashtags");

        JSONArray hits = result.getJSONArray("buckets");

        return customize(hits);
    }

    private static JSONArray customize(JSONArray hits) {
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
