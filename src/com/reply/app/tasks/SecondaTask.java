package com.reply.app.tasks;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class SecondaTask implements Tasklet {
 
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Siamo al " + chunkContext.getStepContext().getStepName() + " con status " + chunkContext.getStepContext().getStepExecution().getStatus() + 
        		" iniziato alle " + chunkContext.getStepContext().getStepExecution().getStartTime());
        
    	System.out.println("Eseguita la seconda Task...");
 
        System.out.println("Recuperando dati delle tabelle...");
        System.out.println("");
        System.out.println("I dati sono stati recuperati dalle tabelle!");
         
        System.out.println("Seconda Task finita...");
        
        System.out.println("Finito lo " + chunkContext.getStepContext().getStepName());                
        
        return RepeatStatus.FINISHED;
    }   
}