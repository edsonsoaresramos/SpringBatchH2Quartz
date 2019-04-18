package com.reply.app.config;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reply.app.tasks.PrimaTask;
import com.reply.app.tasks.SecondaTask;
 
@Configuration
@EnableBatchProcessing
public class BatchConfig {
     
    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;
     
    @Bean
    public Step primoStep(){
        return steps.get("primoStep")
                .tasklet(new PrimaTask())
                .build();
    }
     
    @Bean
    public Step secondoStep(){
        return steps.get("secondoStep")
                .tasklet(new SecondaTask())
                .build();
    }  
     
    @Bean(name="primoJob")
    public Job primoJob(){
        return jobs.get("primoJob")
                .start(primoStep())
                .next(secondoStep())
                .build();
    }
     
    @Bean(name="secondoJob")
    public Job secondoJob(){
        return jobs.get("secondoJob")
                .flow(primoStep())
                .build()
                .build();
    }
}