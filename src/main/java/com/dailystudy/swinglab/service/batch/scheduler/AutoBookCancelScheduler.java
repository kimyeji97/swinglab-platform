package com.dailystudy.swinglab.service.batch.scheduler;

import com.dailystudy.swinglab.service.batch.job.AutoBookCancelJobConfig;
import com.dailystudy.swinglab.service.framework.core.LoggerConst;
import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AutoBookCancelScheduler
{
    private final JobLauncher jobLauncher;
    private final AutoBookCancelJobConfig autoBookCancelJobConfig;

    //    @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(cron = SwinglabConst.CRON_1MIN)
    public void runJob ()
    {
        String jobName = autoBookCancelJobConfig.job().getName();
        long startTime = new Date().getTime();
        LoggerConst.BATCH_LOGGER.info(LoggerConst.BATCH_START_LOG_FM, jobName);
        try
        {
            Map<String, JobParameter<?>> map = new HashMap<>();
            map.put("nowTime", new JobParameter<>(LocalDateTime.now(), LocalDateTime.class));
            JobParameters jobParameters = new JobParameters(map);
            jobLauncher.run(autoBookCancelJobConfig.job(), jobParameters);
        } catch (Exception e)
        {
            LoggerConst.BATCH_LOGGER.error(LoggerConst.BATCH_ERROR_LOG_FM, jobName, e.getMessage());
            LoggerConst.BATCH_LOGGER.error(e.getMessage(), e);
        }
        long endTime = new Date().getTime();
        LoggerConst.BATCH_LOGGER.info(LoggerConst.BATCH_END_LOG_FM, jobName, StringUtils.join(endTime - startTime, "ms"));
    }

}
