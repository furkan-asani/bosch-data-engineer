package com.bosch.datasynchronization;

import jakarta.persistence.Id;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataSyncJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(DataSyncJob.class);

    @Autowired
    private DataSynchronizer _dataSynchronizer;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Executing Sync Job: {}", new Date());

        try {
            _dataSynchronizer.synchronizeData();

        } catch (Exception e) {
            logger.error("Error while synchronizing: {}", new Date(), e);
        }

        logger.info("Successfully finished synchronization run");
    }
}
