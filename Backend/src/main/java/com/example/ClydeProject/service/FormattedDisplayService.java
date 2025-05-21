package com.example.ClydeProject.service;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FormattedDisplayService
{
    public List<List<String>> generateFormattedDisplayLines(List<Map<String, Object>> locationResults,FilterMappingService.FilterConfig filterConfig,FieldMappingService fieldMappingService)
    {
        List<List<String>> finalFormattedLines = new ArrayList<>();

        for (Map<String, Object> record : locationResults)
        {
            List<String> allFormattedLines = new ArrayList<>();
            Map<String, String> fieldValues = new HashMap<>();

            for (FilterMappingService.FieldConfig field : filterConfig.getFields())
            {
                String dbFieldName = field.getDbFieldName();
                String fieldName = field.getFieldName();
                String mapName = field.getMapName();
                Object value = record.get(dbFieldName);
                String displayFormat = field.getDisplayFormat();

                if (mapName != null && !mapName.equals("NULL"))
                {
                    value = fieldMappingService.getMappedValue(mapName, value.toString());
                }
                else
                {
                    value = String.format(displayFormat,value);
                }
                fieldValues.put(fieldName, value.toString());
            }

            List<String> formattedDisplayInfo = filterConfig.getFormattedDisplayLines();

            for (String template : formattedDisplayInfo)
            {
                String formattedLine = replaceFieldPlaceholders(template, fieldValues);
                allFormattedLines.add(formattedLine);
            }
            finalFormattedLines.add(allFormattedLines);
        }
        return finalFormattedLines;
    }
    private String replaceFieldPlaceholders(String template, Map<String, String> fieldValues)
    {
        return StringSubstitutor.replace(template, fieldValues);
    }
}