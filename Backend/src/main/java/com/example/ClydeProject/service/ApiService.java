package com.example.ClydeProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ClydeProject.repo.CustomQueryRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiService
{
    @Autowired
    private CustomQueryRepository customQueryRepository;

    @Autowired
    private ConfigurationService configService;

    @Autowired
    private FieldMappingService fieldMappingService;

    public int getCountForWords(List<String> words, List<String> locations)
    {
        System.out.println("Word inside getCountForWords: " + words);
        System.out.println("Locations inside getCountForWords: " + locations);
        List<Long> results = customQueryRepository.findRecNumRec("pjy0b_recnum_acc_la", "pjy0b_word_account_la", words);
        return results.size();
    }

    /*
    public List<Map<String, Object>> displayMasterTable(List<String> words, List<String> locations)
    {
        List<Map<String, Object>> results = customQueryRepository.displayMasterTable("pjy0b_master_acc_la","pjy0b_recnum_acc_la", "pjy0b_word_account_la", words);
        return results;
    }
    */

    public List<Map<String, Object>> displayMasterTable(List<String> words, List<String> locations)
    {
        List<Map<String, Object>> allResults = new ArrayList<>();

        for (String location : locations)
        {
            ConfigurationService.FilterConfig filterConfig = configService.getFilterConfig(location);

            if (filterConfig != null)
            {
                String dictionaryTable = mapTableName(filterConfig.getDictionaryTable());
                String recnumTable = mapTableName(filterConfig.getRecnumTable());
                String masterTable = mapTableName(filterConfig.getMasterTable());

                System.out.println("Dictionary Table: " + dictionaryTable);
                System.out.println("Recnum Table: " + recnumTable);
                System.out.println("Master Table: " + masterTable);

                List<Map<String, Object>> locationResults = customQueryRepository.displayMasterTable(masterTable, recnumTable, dictionaryTable, words);
                List<Map<String, Object>> formattedResults = formatResults(locationResults, filterConfig);
                allResults.addAll(locationResults);
            }
        }
        return allResults;
    }

    private String mapTableName(String configTableName)
    {
        String[] parts = configTableName.toLowerCase().split("\\.");

        if (parts.length < 6)
        {
            return configTableName;
        }
    
        StringBuilder result = new StringBuilder("pjy0b_");
    
        if (parts[3].equals("word"))
        {
            result.append("word_");
            if (parts[4].equals("account"))
            {
                result.append("account_");
            }
        }
        else if (parts[3].equals("recnum"))
        {
            result.append("recnum_");
            if (parts[4].equals("account"))
            {
                result.append("acc_");
            }
        }
        else if (parts[3].equals("master"))
        {
            result.append("master_");
            if (parts[4].equals("account"))
            {
                result.append("acc_");
            }
        }
    
        result.append(parts[parts.length - 1]);
        return result.toString();
    }
    public void getFieldValueFromKey()
    {
        String value = fieldMappingService.getMappedValue("ACCT-STATUS", "1");
        System.out.println(value);
    }
    private List<Map<String, Object>> formatResults(List<Map<String, Object>> rawResults, ConfigurationService.FilterConfig filterConfig)
    {
        List<Map<String, Object>> formattedResults = new ArrayList<>();
        for (Map<String, Object> rawResult : rawResults) 
        {
            Map<String, Object> formattedResult = new HashMap<>();
            for (int i = 0; i < filterConfig.getDisplayLines().size(); i++)
            {
                String displayLine = filterConfig.getDisplayLines().get(i);
                String formattedLine = formatLine(displayLine, rawResult, filterConfig.getFields());
                formattedResult.put("BNA" + (i + 1), formattedLine);
            }
            formattedResults.add(formattedResult);
        }
        return formattedResults;
    }
    private String formatLine(String template, Map<String, Object> data, List<ConfigurationService.FieldConfig> fields)
    {
        StringBuilder result = new StringBuilder(template);
        
        for (ConfigurationService.FieldConfig field : fields)
        {
            if (field.getLine() == result.length() && field.getMapName().equals("NULL"))
            {
                String value = String.valueOf(data.get(field.getDisplayFormat().substring(2)));
                int startIndex = field.getColumn() - 1;
                int endIndex = Math.min(startIndex + value.length(), result.length());
                result.replace(startIndex, endIndex, value);
            }
        }
        return result.toString();
    }
}