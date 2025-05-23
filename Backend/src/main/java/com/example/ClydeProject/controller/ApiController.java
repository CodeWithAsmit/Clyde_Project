package com.example.ClydeProject.controller;

import org.springframework.web.bind.annotation.*;
import com.example.ClydeProject.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")

public class ApiController
{
    @Autowired
    private ApiService apiService;

    private static class WordsAndLocations
    {
        final List<String> words;
        final List<String> locations;

        WordsAndLocations(List<String> words, List<String> locations)
        {
            this.words = words;
            this.locations = locations;
        }
    }

    @PostMapping("/get_count_for_words")
    public ResponseEntity<Map<String, Object>> getWordCount(@RequestBody Map<String, Object> requestBody)
    {        
        try
        {
            String newWord = null;
            
            WordsAndLocations extracted = extractWordsAndLocations(requestBody);
            List<String> words = extracted.words;
            List<String> locations = extracted.locations;
            
            if (requestBody.get("word") instanceof String)
            {
                newWord = (String) requestBody.get("word");
            }
            if (newWord != null)
            {
                words.add(newWord);
            }

            int count = apiService.getCountForWords(words, locations);

            Map<String, Object> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
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
            WordsAndLocations extracted = extractWordsAndLocations(requestBody);
            List<String> words = extracted.words;
            List<String> locations = extracted.locations;

            List<List<String>> result = apiService.displayMasterTable(words, locations);
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private WordsAndLocations  extractWordsAndLocations(Map<String, Object> requestBody)
    {
        List<String> words = new ArrayList<>();
        List<String> locations = new ArrayList<>();

        if (requestBody.get("buffer") instanceof List<?> bufferList)
        {
            for (Object item : bufferList)
            {
                if (item instanceof String str)
                {
                    words.add(str);
                }
            }
        }

        if (requestBody.get("locations") instanceof List<?> locationsList)
        {
            for (Object item : locationsList)
            {
                if (item instanceof String str)
                {
                    locations.add(str);
                }
            }
        }
        return new WordsAndLocations(words, locations);
    }
}