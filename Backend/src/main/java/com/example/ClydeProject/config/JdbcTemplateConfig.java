package com.example.ClydeProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig
{
    @Bean
    public NamedParameterJdbcTemplate refTemplate(DataSource dataSource)
    {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}