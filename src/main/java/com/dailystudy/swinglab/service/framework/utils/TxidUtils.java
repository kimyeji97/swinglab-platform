package com.dailystudy.swinglab.service.framework.utils;

import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.MessageHeaders;
import org.slf4j.MDC;

/**
 * Logging에 필요한 txid 값을 세팅해주는 Util 클래스.
 */
public class TxidUtils
{
    public static final String getTxid(HttpServletRequest request)
    {
        return request.getAttribute(SwinglabConst.TXID).toString();
    }

    public static final String getTxid(MessageHeaders messageHeaders)
    {
        if (messageHeaders != null)
        {
            return messageHeaders.get(SwinglabConst.TXID, String.class);
        } else
        {
            return null;
        }
    }

    public static String getOrInitializeTxid()
    {
        String txid = MDC.get(SwinglabConst.TXID);

        if (txid == null)
        {
            txid = String.valueOf(UUIDGenerator.generateTxid());
            MDC.put(SwinglabConst.TXID, txid);
        }

        return txid;
    }

    public static boolean hasTxid()
    {
        return MDC.get(SwinglabConst.TXID) != null;
    }

    public static String initializeTxid()
    {
        String GTXID = String.valueOf(UUIDGenerator.generateTxid());
        MDC.put(SwinglabConst.TXID, GTXID);

        return GTXID;
    }

    public static void setTxid(Long txid)
    {
        MDC.put(SwinglabConst.TXID, String.valueOf(txid));
    }

    public static void setTxid(String txid)
    {
        MDC.put(SwinglabConst.TXID, txid);
    }

    public static void clearTxid()
    {
        MDC.remove(SwinglabConst.TXID);
    }

    public static String getTxid()
    {
        return MDC.get(SwinglabConst.TXID);
    }

    public static String createTxid()
    {
        return String.valueOf(UUIDGenerator.generateTxid());
    }

    public static String initializeTxidIfNotAbsent(HttpServletRequest request)
    {
        String transactionId = request.getHeader(SwinglabConst.TXID);
        if (transactionId == null && hasTxid())
        {
            transactionId = getTxid();
        }

        if (StringUtils.isEmpty(transactionId))
        {
            transactionId = TxidUtils.createTxid();
        }

        // txid 세팅.
        if (hasTxid() == false)
        {
            setTxid(transactionId);
        }
        if (request.getAttribute(SwinglabConst.TXID) == null)
        {
            request.setAttribute(SwinglabConst.TXID, transactionId);
        }

        return transactionId;
    }
}
