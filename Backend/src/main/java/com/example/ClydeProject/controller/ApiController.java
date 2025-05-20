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

    @PostMapping("/get_count_for_words")
    public ResponseEntity<Map<String, Object>> getWordCount(@RequestBody Map<String, Object> requestBody)
    {
        System.out.println("Request Body inside getWordCount: " + requestBody);
        
        try
        {
            List<String> words = null;
            String newWord = null;
            List<String> locations = null;

            if (requestBody.get("buffer") instanceof List<?>)
            {
                words = new ArrayList<>();
                List<?> bufferList = (List<?>) requestBody.get("buffer");
                for (Object item : bufferList)
                {
                    if (item instanceof String)
                    {
                        words.add((String) item);
                    }
                }
            }

            if (requestBody.get("word") instanceof String)
            {
                newWord = (String) requestBody.get("word");
            }

            if (requestBody.get("locations") instanceof List<?>)
            {
                locations = new ArrayList<>();
                List<?> locationsList = (List<?>) requestBody.get("locations");
                for (Object item : locationsList)
                {
                    if (item instanceof String)
                    {
                        locations.add((String) item);
                    }
                }
            }

            if (words == null)
            {
                words = new ArrayList<>();
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
    public ResponseEntity<List<Map<String, Object>>> displayRecords(@RequestBody Map<String, Object> requestBody)
    {
        System.out.println("Request Body inside displayRecords: " + requestBody);

        try
        {
            List<String> words = new ArrayList<>();

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

            List<String> locations = new ArrayList<>();
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

            List<Map<String, Object>> result = apiService.displayMasterTable(words,locations);
            System.out.println("Mapped Results: " + result);
            System.out.println("Total Results: " + result.size());
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}