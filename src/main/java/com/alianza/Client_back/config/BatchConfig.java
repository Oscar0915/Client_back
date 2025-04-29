package com.alianza.Client_back.config;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.alianza.Client_back.entity.Client;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JdbcCursorItemReader<Client> clientReader;

    @Autowired
    private LineAggregator<Client> clientLineAggregator;

    @Bean
    public FlatFileItemWriter<Client> clientWriter() {
        FlatFileItemWriter<Client> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("clients.csv"));
        writer.setLineAggregator(clientLineAggregator);
        return writer;
    }

    @Bean
    public Step exportClientsStep() {
        return stepBuilderFactory.get("exportClientsStep")
                .<Client, Client>chunk(10)
                .reader(clientReader)
                .processor(new com.alianza.Client_back.batch.ClientItemProcessor())
                .writer(clientWriter())
                .build();
    }

    @Bean
    public Job exportClientsJob() {
        return jobBuilderFactory.get("exportClientsJob")
                .start(exportClientsStep())
                .build();
    }
}
