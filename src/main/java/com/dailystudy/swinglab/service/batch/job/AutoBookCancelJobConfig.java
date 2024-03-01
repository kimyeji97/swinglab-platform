package com.dailystudy.swinglab.service.batch.job;

import com.dailystudy.swinglab.service.batch.QuerydslPagingItemReader;
import com.dailystudy.swinglab.service.business.common.domain.entity.zone.ZoneBookHist;
import com.dailystudy.swinglab.service.framework.core.LoggerConst;
import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import com.dailystudy.swinglab.service.framework.utils.DateUtil;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

import static com.dailystudy.swinglab.service.business.common.domain.entity.zone.QZoneBookHist.zoneBookHist;
import static com.dailystudy.swinglab.service.business.common.domain.entity.zone.QZoneUsageHist.zoneUsageHist;

@Configuration
@RequiredArgsConstructor
public class AutoBookCancelJobConfig
{
    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager transactionManager;

    private static final String JOB_NAME = "autoBookCancelJop";
    private static final String STEP_NAME = JOB_NAME + "Step";

    private LocalDateTime targetTime;

    @Bean
    public Job job ()
    {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(step())
                .build();
    }

    @Bean
    @JobScope
    public Step step ()
    {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<ZoneBookHist, ZoneBookHist>chunk(SwinglabConst.CHK_SIZE, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<ZoneBookHist> reader ()
    {
        targetTime = LocalDateTime.now().minusMinutes(SwinglabConst.DF_MIN);
        LoggerConst.BATCH_LOGGER.info(LoggerConst.BATCH_WORK_LOG_FM, JOB_NAME, StringUtils.join(
                "Target is bookings before ", DateUtil.formatDate(targetTime, SwinglabConst.DT_FORMAT)
        ));
        return new QuerydslPagingItemReader<>(entityManagerFactory,
                SwinglabConst.CHK_SIZE,
                queryFactory -> queryFactory
                        .selectFrom(zoneBookHist)
                        .where(zoneBookHist.bookStDt.lt(targetTime)
                                .and(zoneBookHist.bookCnclYn.isFalse())
                                .and(JPAExpressions
                                        .selectFrom(zoneUsageHist)
                                        .where(zoneUsageHist.bookId.eq(zoneBookHist.bookId))
                                        .notExists()
                                )
                        )
        );
    }

    @Bean
    @StepScope
    public ItemProcessor<ZoneBookHist, ZoneBookHist> processor ()
    {
        return item -> {
            LoggerConst.BATCH_LOGGER.info(LoggerConst.BATCH_WORK_LOG_FM, JOB_NAME, StringUtils.join(
                    " Automatically canceled => ", item.getBookId()
            ));
            item.setBookCnclYn(true);
            item.setAutoBookCnclYn(true);
            return item;
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<ZoneBookHist> writer ()
    {
        return new JpaItemWriterBuilder<ZoneBookHist>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
