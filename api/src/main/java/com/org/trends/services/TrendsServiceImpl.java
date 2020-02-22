package com.org.trends.services;

import com.org.trends.util.Utils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TrendsServiceImpl implements TrendsService {

    private RestHighLevelClient client;
    @Value("${index}")
    private String index;

    @Autowired
    public TrendsServiceImpl(RestHighLevelClient client) {
        this.client = client;
    }

    public JSONArray search(String term) throws IOException {
        term = Utils.termNormalizer(term);

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("hashtags", term));
        searchSourceBuilder.aggregation(
                AggregationBuilders.terms("top_hashtags").field("hashtags")
        );
        searchRequest.source(searchSourceBuilder);

        SearchResponse resp = client.search(searchRequest, RequestOptions.DEFAULT);

        return Utils.convertToJSON(resp.toString());
    }

}
