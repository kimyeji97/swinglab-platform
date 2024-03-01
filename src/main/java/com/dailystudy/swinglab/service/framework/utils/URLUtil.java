package com.dailystudy.swinglab.service.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


/**
 * URL 유틸리티
 *
 * @author yjkim
 */
@Slf4j
public class URLUtil {

    public static final String PRE_HTTP ="http://";
    public static final String PRE_HTTPS = "https://";

    public static String addQueryParam(String uri, String key, String value) {
        if (StringUtils.isBlank(uri) || StringUtils.isBlank(key)) {
            return "";
        }

        if (!(uri.contains(PRE_HTTP) || uri.contains(PRE_HTTPS)) && !uri.startsWith("forward:"))
        {
            uri = PRE_HTTP + uri;
        }

        if(uri.startsWith("redirect:"))
        {
            uri = uri.replace("redirect:", "");
        }

        if(uri.startsWith("forward:"))
        {
            uri = uri.replace("forward:","");
        }

        try
        {
            StringBuffer sb = new StringBuffer();
            sb.append(key).append("=");
            if(value != null)
            {
                sb.append(value);
            }

            URI oldUri = new URI(uri);

            String newQuery = oldUri.getQuery();

            if (newQuery == null) {
                newQuery = sb.toString();
            } else {
                newQuery += "&" + sb.toString();
            }

            return new URI(oldUri.getScheme(), oldUri.getAuthority(),
                    oldUri.getPath(), newQuery, oldUri.getFragment()).toString();
        }
        catch(URISyntaxException e)
        {
            log.error(e.getMessage(), e);
            return uri;
        }
    }
    
    public static <T> T convertStringToMap(String str, Class<T> classType) {
        Map<String, String> map = new HashMap<>();
        String[] keyValues = str.split("&");
        for (String keyValue : keyValues) {
            String[] pair = keyValue.split("=");
            if (pair.length == 2) {
                String key = pair[0];
                String value = pair[1];
                map.put(key, value);
            }
        }
//      JsonUtil.convertObjectMultiValueMap(keyValues);
//      return map;
        T result = null;
        try {
            result = JsonUtil.convertObjectWithUnkown(map, classType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        
        return result;
    }
    
}
