package com.example.ClydeProject.repo;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.sql.DataSource;
 
@Repository
public class CustomQueryRepository
{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;
 
    public List<Long> findRecNumRec(String recNameTable, String wordsTable, List<String> lastWords)
    {
        System.out.println("Last Words inside findRecNumRec: " + lastWords);

        if (!recNameTable.matches("\\w+") || !wordsTable.matches("\\w+"))
        {
            throw new IllegalArgumentException("Invalid table name");
        }

        String schema = "vsam.";
        String fullRecTable = schema + recNameTable;
        String fullWordsTable = schema + wordsTable;

        String inClause = IntStream.range(0, lastWords.size())
            .mapToObj(i -> ":word" + i)
            .collect(Collectors.joining(", "));

            String sql = "SELECT A.REC_NUM_REC FROM " + fullRecTable + " A, " + fullWordsTable + " B " +
                        "WHERE B.LAST_WORD IN (" + inClause + ") " +
                        "AND A.DBKEY BETWEEN FIRST_REC AND FIRST_REC+REC_COUNT - 1 " +
                        "GROUP BY A.REC_NUM_REC HAVING COUNT(*) = :wordCount";

        Query query = entityManager.createNativeQuery(sql);

        for (int i = 0; i < lastWords.size(); i++)
        {
            query.setParameter("word" + i, lastWords.get(i));
            System.out.println("word" + i + ": " + lastWords.get(i));
        }

        query.setParameter("wordCount", lastWords.size());
        System.out.println("wordCount: " + lastWords.size());

        @SuppressWarnings("unchecked")
        List<Number> result = query.getResultList();

        return result.stream()
             .map(Number::longValue)
             .collect(Collectors.toList());
        //return query.getResultList();
    }

    public List<Map<String, Object>> displayMasterTable(String masterTable, String recNumTable, String wordTable, List<String> lastWords)
    {
        System.out.println("Last Words inside displayMasterTable: " + lastWords);

        if (!masterTable.matches("\\w+") || !recNumTable.matches("\\w+") || !wordTable.matches("\\w+"))
        {
            throw new IllegalArgumentException("Invalid table name");
        }

        String schema = "vsam.";
        String fullRecTable = schema + recNumTable;
        String fullWordsTable = schema + wordTable;
        String fullMasterTable = schema + masterTable;

        String inClause = String.join(", ", Collections.nCopies(lastWords.size(), "?"));

        String sql = "SELECT * FROM " + fullMasterTable + " WHERE DBKEY IN (" +
                    "SELECT A.REC_NUM_REC FROM " + fullRecTable + " A " +
                    "JOIN " + fullWordsTable + " B " +
                    "ON A.DBKEY BETWEEN B.FIRST_REC AND B.FIRST_REC + B.REC_COUNT - 1 " +
                    "WHERE B.LAST_WORD IN (" + inClause + ") " +
                    "GROUP BY A.REC_NUM_REC " +
                    "HAVING COUNT(DISTINCT B.LAST_WORD) = ?" +
                    ")";
        
        List<Map<String, Object>> result = new ArrayList<>();

        try(Connection conn = dataSource.getConnection();PreparedStatement stmt = conn.prepareStatement(sql))
        {
            int i = 1;
            for (String word : lastWords)
            {
                stmt.setString(i++, word);
            }

            stmt.setInt(i, lastWords.size());

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next())
            {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int col = 1; col <= columnCount; col++)
                {
                    String columnName = metaData.getColumnLabel(col);
                    Object value = rs.getObject(col);
                    row.put(columnName, value);
                }
                result.add(row);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error executing query", e);
        }
        return result;
    }
}