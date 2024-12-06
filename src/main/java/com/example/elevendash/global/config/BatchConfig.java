package com.example.elevendash.global.config;

import com.example.elevendash.domain.point.entity.Point;
import com.example.elevendash.domain.point.processor.PointItemProcessor;
import com.example.elevendash.domain.point.reader.PointItemReader;
import com.example.elevendash.domain.point.writer.PointItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;

    private final DataSource dataSource;

    @Bean
    public Job expirePointsJob() {
        return new JobBuilder("expirePointsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(expirePointsStep())
                .build();
    }

    @Bean
    public Step expirePointsStep() {
        return new StepBuilder("expirePointsStep", jobRepository)
                .<Point, Point>chunk(10000)  // Use long for chunk size
                .reader(pointItemReader())
                .processor(pointItemProcessor())
                .writer(pointItemWriter())
                .transactionManager(new DataSourceTransactionManager(dataSource))
                .build();
    }

    @Bean
    public PointItemReader pointItemReader() {
        return new PointItemReader();
    }

    @Bean
    public PointItemProcessor pointItemProcessor() {
        return new PointItemProcessor();
    }

    @Bean
    public PointItemWriter pointItemWriter() {
        return new PointItemWriter();
    }

}