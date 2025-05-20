package com.example.ClydeProject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class FieldMappingService
{
    private Map<String, Map<String, String>> mappings;

    @PostConstruct
    public void init()
    {
        mappings = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = getClass().getResourceAsStream("/fields.json"))
        {
            JsonNode root = mapper.readTree(is);
            JsonNode configs = root.get("configs");

            for (JsonNode config : configs)
            {
                String name = config.get("name").asText();
                Map<String, String> mapValues = new HashMap<>();

                JsonNode mappingsNode = config.get("mappings");
                for (JsonNode mapping : mappingsNode)
                {
                    String key = mapping.get("key").asText();
                    String value = mapping.get("value").asText();
                    mapValues.put(key, value);
                }

                mappings.put(name, mapValues);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getMappedValue(String mapName, String key)
    {
        Map<String, String> mapValues = mappings.get(mapName);
        if (mapValues != null)
        {
            return mapValues.getOrDefault(key, "Unknown");
        }
        return "Unknown";
    }
}