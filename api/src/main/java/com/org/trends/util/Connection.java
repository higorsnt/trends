package com.org.trends.util;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Component
public class Connection {

    public String search(String term) throws IOException {
        term = term.replaceAll("[^\\wÀ-ú\\d]", " ");
        String baseUrl = "http://localhost:9200/trends/";

        URL url = new URL(baseUrl + "_search?filter_path=aggregations.genres.buckets");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(Strings.toString(createQuery(term)));
        osw.flush();
        osw.close();
        os.close();

        connection.connect();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()
        ));

        String inputLine;
        StringBuffer content = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        return content.toString();
    }

    private XContentBuilder createQuery(String term) throws IOException {
        List<String> terms = new ArrayList<>();

        for (String s : term.split(" ")) {
            if (!s.isBlank() || !s.isEmpty()) {
                terms.add(s);
            }
        }

        XContentBuilder query = XContentFactory.jsonBuilder().
                startObject().
                    startObject("query").
                        startObject("bool").
                            startArray("must");

        terms.forEach(s -> {
            try {
                query.startObject().
                            startObject("match").
                                field("hashtags", s).
                            endObject().
                        endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        query.endArray().endObject().endObject();

        query.startObject("aggs").
                    startObject("genres").
                        startObject("terms").
                            field("field", "hashtags").
                        endObject().
                    endObject().
                endObject().
            endObject();

        return query;
    }
}
