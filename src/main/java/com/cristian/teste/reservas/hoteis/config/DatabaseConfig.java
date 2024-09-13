package com.cristian.teste.reservas.hoteis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        // Inicializa com o script para inserir dados
        ResourceDatabasePopulator dataPopulator = new ResourceDatabasePopulator();
        dataPopulator.addScript(new ClassPathResource("static/data.sql"));
        initializer.setDatabasePopulator(dataPopulator);

        return initializer;
    }
}