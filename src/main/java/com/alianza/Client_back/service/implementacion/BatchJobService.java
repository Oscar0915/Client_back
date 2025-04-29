package com.alianza.Client_back.service.implementacion;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;


@Service
public class BatchJobService {

    private final JobLauncher jobLauncher;
    private final Job exportClientsJob;

    public BatchJobService(JobLauncher jobLauncher, Job exportClientsJob) {
        this.jobLauncher = jobLauncher;
        this.exportClientsJob = exportClientsJob;
    }

    public void runExportJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(exportClientsJob, jobParameters);
    }
}

