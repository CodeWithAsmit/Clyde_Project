package com.example.ClydeProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ClydeProject.repo.CustomQueryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiService
{
    @Autowired
    private CustomQueryRepository customQueryRepository;

    @Autowired
    private FilterMappingService configService;

    @Autowired
    private FieldMappingService fieldMappingService;

    @Autowired
    private FormattedDisplayService formattedDisplayService;

    public int getCountForWords(List<String> words, List<String> locations)
    {
        int totalCount = 0;
        for (String location : locations)
        {
            FilterMappingService.FilterConfig filterConfig = configService.getFilterConfig(location);

            if (filterConfig != null)
            {
                String dictionaryTable = mapTableName(filterConfig.getDictionaryTable());
                String recnumTable = mapTableName(filterConfig.getRecnumTable());
                List<Long> results = customQueryRepository.findRecNumRec(recnumTable, dictionaryTable, words);
                totalCount += results.size();
            }
        }
        return totalCount;
    }

    public List<List<String>> displayMasterTable(List<String> words, List<String> locations)
    {
        List<List<String>> allFormattedResults = new ArrayList<>();

        for (String location : locations)
        {
            FilterMappingService.FilterConfig filterConfig = configService.getFilterConfig(location);

            if (filterConfig != null)
            {
                String dictionaryTable = mapTableName(filterConfig.getDictionaryTable());
                String recnumTable = mapTableName(filterConfig.getRecnumTable());
                String masterTable = mapTableName(filterConfig.getMasterTable());

                List<Map<String, Object>> locationResults = customQueryRepository.displayMasterTable(masterTable, recnumTable, dictionaryTable, words);
                List<List<String>> formattedLines = formattedDisplayService.generateFormattedDisplayLines(locationResults, filterConfig, fieldMappingService);        
                allFormattedResults.addAll(formattedLines);
            }
        }
        return allFormattedResults;
    }

    private String mapTableName(String configTableName)
    {
        String[] parts = configTableName.toLowerCase().split("\\.");
    
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
}