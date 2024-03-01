package com.dailystudy.swinglab.service.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerConst
{
    public static final Logger BATCH_LOGGER = LoggerFactory.getLogger("SWINGLAB_BATCH");
    public static final Logger PAYLOAD_LOGGER = LoggerFactory.getLogger("SWINGLAB_HTTP_PAYLOAD");
    public static final Logger ERROR_LOGGER = LoggerFactory.getLogger("SWINGLAB_ERROR");

    public static final String BATCH_START_LOG_FM = "[Batch-{}] START";
    public static final String BATCH_WORK_LOG_FM = "[Batch-{}] WORKING.. : {}";
    public static final String BATCH_ERROR_LOG_FM = "[Batch-{}] ERROR : {}";
    public static final String BATCH_END_LOG_FM = "[Batch-{}] END : {}";
}
