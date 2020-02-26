package com.org.trends.controllers;

import com.org.trends.services.TrendsService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/search")
public class TrendsController {

    private TrendsService trendsService;

    @Autowired
    public TrendsController(TrendsService trendsService) {
        this.trendsService = trendsService;
    }

    @GetMapping("/{term}")
    public ResponseEntity<String> search(@PathVariable("term") String term) {
        JSONArray array = new JSONArray();

        try {
            array = trendsService.search(term);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(array.toString(), HttpStatus.OK);
    }
}
