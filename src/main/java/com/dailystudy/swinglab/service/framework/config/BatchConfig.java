package com.dailystudy.swinglab.service.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableScheduling
public class BatchConfig
{
    @Bean
    public PlatformTransactionManager transactionManager ()
    {
        return new JpaTransactionManager();
    }
}

