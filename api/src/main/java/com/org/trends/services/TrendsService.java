package com.org.trends.services;

import org.json.JSONArray;

import java.io.IOException;

public interface TrendsService {

    JSONArray search(String term) throws IOException;

}
