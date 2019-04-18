package com.reply.app.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ReplyQuartzJob extends QuartzJobBean {
 
	private final Logger replyQuartzJobLogging = LoggerFactory.getLogger(ReplyQuartzJob.class);
	
    private String nomeDelJob;
    private JobLauncher jobLauncher;
    private JobLocator jobLocator;
 
    public String getNomeDelJob() {
        return nomeDelJob;
    }
 
    public void setNomeDelJob(String nomeDelJob) {
        this.nomeDelJob = nomeDelJob;
    }
 
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
        	replyQuartzJobLogging.info("...Creando il contexto dell'applicazione con Quartz Scheduler...");
            ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
            jobLocator = (JobLocator) applicationContext.getBean(JobLocator.class);
            jobLauncher = (JobLauncher) applicationContext.getBean(JobLauncher.class);

            Job job = jobLocator.getJob(nomeDelJob);
            
            long idCurrentMillis = System.currentTimeMillis();
            JobParameters params = new JobParametersBuilder().addString("JobName", getNomeDelJob().concat(" and JobId= "+String.valueOf(idCurrentMillis))).toJobParameters();
            
            replyQuartzJobLogging.info("...Eseguendo il job...");
            jobLauncher.run(job, params);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}