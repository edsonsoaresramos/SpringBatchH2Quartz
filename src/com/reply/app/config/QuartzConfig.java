package com.reply.app.config;
import java.io.IOException;
import java.util.Properties;
 
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.reply.app.jobs.ReplyQuartzJob;
 
@Configuration
public class QuartzConfig {  
    private static final String QUARTZ_PROPERTIES = "/resources/quartz.properties";
	private static final String APPLICATION_CONTEXT = "applicationContext";
	private static final String SECONDO_JOB_TRIGGER = "secondoJobTrigger";
	private static final String PRIMO_JOB_TRIGGER = "primoJobTrigger";
	private static final String DESCRIZIONE_SECONDO_JOB = "Secondo Job di Reply con Quartz Scheduler";
	private static final String SECONDO_JOB = "secondoJob";
	private static final String DESCRIZIONE_PRIMO_JOB = "Primo Job di Reply con Quartz Scheduler";
	private static final String DESCRIZIONE_DEL_JOB = "descrizioneDelJob";
	private static final String PRIMO_JOB = "primoJob";
	private static final String NOME_DEL_JOB = "nomeDelJob";

	@Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        
        return jobRegistryBeanPostProcessor;
    }
 
    @Bean
    public JobDetail primoJobDettaglio() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(NOME_DEL_JOB, PRIMO_JOB);
        jobDataMap.put(DESCRIZIONE_DEL_JOB, DESCRIZIONE_PRIMO_JOB);
         
        return JobBuilder.newJob(ReplyQuartzJob.class)
                .withIdentity(PRIMO_JOB,null)
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }
     
    @Bean
    public JobDetail secondoJobDettaglio() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(NOME_DEL_JOB, SECONDO_JOB);
        jobDataMap.put(DESCRIZIONE_DEL_JOB, DESCRIZIONE_SECONDO_JOB);
         
        return JobBuilder.newJob(ReplyQuartzJob.class)
                .withIdentity(SECONDO_JOB,null)
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }
     
    @Bean
    public Trigger primoJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(10)
                .repeatForever();
 
        return TriggerBuilder
                .newTrigger()
                .forJob(primoJobDettaglio())
                .withIdentity(PRIMO_JOB_TRIGGER,null)
                .withSchedule(scheduleBuilder)
                .build();
    }
     
    @Bean
    public Trigger secondoJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(20)
                .repeatForever();
 
        return TriggerBuilder
                .newTrigger()
                .forJob(secondoJobDettaglio())
                .withIdentity(SECONDO_JOB_TRIGGER,null)
                .withSchedule(scheduleBuilder)
                .build();
    }
     
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException, SchedulerException {
        return fillSchedulerFactoryObject();
    }
    
    private SchedulerFactoryBean fillSchedulerFactoryObject()  throws IOException, SchedulerException {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(primoJobTrigger(), secondoJobTrigger());
        scheduler.setQuartzProperties(quartzProperties());
        scheduler.setJobDetails(primoJobDettaglio(), secondoJobDettaglio());
        scheduler.setApplicationContextSchedulerContextKey(APPLICATION_CONTEXT);
        
        return scheduler;
    }
    
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}