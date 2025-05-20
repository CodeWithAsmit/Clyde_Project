package com.example.ClydeProject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilterMappingService
{
    private Map<String, FilterConfig> filterConfigs;
    
    @PostConstruct
    public void init()
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("main.json");
            
            JsonNode root = mapper.readTree(resource.getInputStream());
            JsonNode configs = root.get("configs");
            
            filterConfigs = new HashMap<>();

            for (JsonNode node : configs)
            {
                if ("FILTER".equals(node.get("type").asText()))
                {
                    FilterConfig config = mapper.treeToValue(node, FilterConfig.class);
                    filterConfigs.put(config.getName(), config);
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error loading configuration", e);
        }
    }

    public FilterConfig getFilterConfig(String locationName)
    {
        return filterConfigs.get(locationName);
    }

    public static class ConfigRoot
    {
        private List<FilterConfig> configs;

        public List<FilterConfig> getConfigs()
        {
            return configs;
        }
        public void setConfigs(List<FilterConfig> configs)
        {
            this.configs = configs;
        }
    }

    public static class FilterConfig
    {
        private String type;
        private String name;
        private String dictionaryTable;
        private String recnumTable;
        private String masterTable;
        private int minWordSize;
        private int maxWordSize;
        private int masterRecSize;
        private int linesToDisplay;
        private int maxCharsPerLine;
        private List<String> displayLines;
        private int fieldsCount;
        private List<FieldConfig> fields;

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getDictionaryTable()
        {
            return dictionaryTable;
        }

        public void setDictionaryTable(String dictionaryTable)
        {
            this.dictionaryTable = dictionaryTable;
        }

        public String getRecnumTable()
        { 
            return recnumTable; 
        }

        public void setRecnumTable(String recnumTable)
        {
            this.recnumTable = recnumTable;
        }

        public String getMasterTable()
        {
            return masterTable;
        }

        public void setMasterTable(String masterTable)
        {
            this.masterTable = masterTable;
        }

        public int getMinWordSize()
        {
            return minWordSize;
        }

        public void setMinWordSize(int minWordSize)
        {
            this.minWordSize = minWordSize;
        }

        public int getMaxWordSize()
        {
            return maxWordSize;
        }

        public void setMaxWordSize(int maxWordSize)
        {
            this.maxWordSize = maxWordSize;
        }

        public int getMasterRecSize()
        {
            return masterRecSize;
        }

        public void setMasterRecSize(int masterRecSize)
        {
            this.masterRecSize = masterRecSize;
        }

        public int getLinesToDisplay()
        {
            return linesToDisplay;
        }

        public void setLinesToDisplay(int linesToDisplay)
        {
            this.linesToDisplay = linesToDisplay;
        }

        public int getFieldCount()
        {
            return fieldsCount;
        }

        public void setFieldsCount(int fieldsCount)
        {
            this.fieldsCount = fieldsCount;
        }

        public List<String> getDisplayLines()
        {
            return displayLines;
        }

        public void setDisplayLines(List<String> displayLines)
        {
            this.displayLines = displayLines;
        }

        public int getMaxCharsPerLine()
        {
            return maxCharsPerLine;
        }

        public void setMaxCharsPerLine(int maxCharsPerLine)
        {
            this.maxCharsPerLine = maxCharsPerLine;
        }

        public List<FieldConfig> getFields()
        {
            return fields;
        }

        public void setFields(List<FieldConfig> fields)
        {
            this.fields = fields;
        }
    }

    public static class FieldConfig
    {
        private int offset;
        private String inputFormat;
        private String mapName;
        private String displayFormat;
        private int line;
        private int column;

        public int getOffset()
        {
            return offset;
        }

        public void setOffset(int offset)
        {
            this.offset = offset;
        }

        public String getInputFormat()
        {
            return inputFormat;
        }

        public void setInputFormat(String inputFormat)
        {
            this.inputFormat = inputFormat;
        }

        public String getMapName()
        {
            return mapName;
        }

        public void setMapName(String mapName)
        {
            this.mapName = mapName;
        }

        public String getDisplayFormat()
        {
            return displayFormat;
        }

        public void setDisplayFormat(String displayFormat)
        {
            this.displayFormat = displayFormat;
        }

        public int getLine()
        {
            return line;
        }

        public void setLine(int line)
        {
            this.line = line;
        }

        public int getColumn()
        {
            return column;
        }

        public void setColumn(int column)
        {
            this.column = column;
        }
    }
}