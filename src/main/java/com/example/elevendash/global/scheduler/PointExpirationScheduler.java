package com.example.elevendash.global.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointExpirationScheduler {

    private final JobLauncher jobLauncher;
    private final Job expirePointsJob;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에
    public void expirePoints() throws JobExecutionException {
        jobLauncher.run(expirePointsJob, new JobParameters());
    }
}