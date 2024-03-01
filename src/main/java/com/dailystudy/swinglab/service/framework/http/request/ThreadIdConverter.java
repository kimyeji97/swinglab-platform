package com.dailystudy.swinglab.service.framework.http.request;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * It's used by logback logger library to generate number format thread id.
 *
 * logback-spring.xml 에서 아래와 같이 사용합니다.
 * <pre>
 *     <conversionRule conversioxxwnWord="tid" converterClass="com.macrogen.core.logging.logback.ThreadIdConverter"/>
 *     ... (%tid)
 *     <property name="DEBUG_PATTERN" value="%d{HH:mm:ss.SSS} [%tid]- [%-5level] %class{0}.%M:%L - %msg%n"/>
 * </pre>
 * @author GG
 */
public class ThreadIdConverter extends ClassicConverter
{
    private static int nextId = 0;

    private static final ThreadLocal<String> threadId = ThreadLocal.withInitial(() -> {
        int nextId = nextId();
        return String.format("%05d", nextId);
    });

    private static synchronized int nextId()
    {
		return ++nextId;
    }
    @Override
    public String convert(ILoggingEvent event)
    {
        return threadId.get();
    }
}
