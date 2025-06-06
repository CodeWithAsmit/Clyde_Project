package com.example.ClydeProject.controller;

import org.springframework.web.bind.annotation.*;
import com.example.ClydeProject.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")

public class ApiController
{
    @Autowired
    private ApiService apiService;

    @PostMapping("/get_count_for_words")
    public ResponseEntity<Map<String, Object>> getWordCount(@RequestBody Map<String, Object> requestBody)
    {        
        try
        {   
            List<String> words = extractList(requestBody, "wordBuffer");
            List<String> locations = extractList(requestBody, "locations");
            List<String> containsWord = extractList(requestBody, "containsWord");

            int count = apiService.countMatchingRecords(words, containsWord, locations);
            return ResponseEntity.ok(Map.of("count", count));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/display_records")
    public ResponseEntity<List<List<String>>> displayRecords(@RequestBody Map<String, Object> requestBody)
    {
        try
        {
            List<String> words = extractList(requestBody, "wordBuffer");
            List<String> locations = extractList(requestBody, "locations");
            List<String> containsWord = extractList(requestBody, "containsWord");

            List<List<String>> result = apiService.displayMasterTable(words, containsWord, locations);
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private List<String> extractList(Map<String, Object> map, String key)
    {
        if (map.get(key) instanceof List<?> list)
        {
            return list.stream()
                       .filter(String.class::isInstance)
                       .map(String.class::cast)
                       .distinct()
                       .toList();
        }
        return List.of();
    }
}