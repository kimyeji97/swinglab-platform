package com.dailystudy.swinglab.service.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonNodeUtil
{
    public static String jsonNodeToString(JsonNode node, String... fieldName)
    {
        if(node == null || fieldName == null || fieldName.length == 0) {
            return null;
        }
        
        if(fieldName.length == 1) {
            return node.has(fieldName[0]) ? node.get(fieldName[0]).asText() : null;
        }
        else if(node.has(fieldName[0])) {
            JsonNode tmp = node.get(fieldName[0]);
            
            for(int i = 1; i < fieldName.length; i++) {
                if(tmp == null) {
                    return null;
                }
                
                if(tmp.has(fieldName[i])) {
                    tmp = tmp.get(fieldName[i]);
                }
            }
            
            return tmp != null ? tmp.asText() : null;
        }
        
        return null;
        
//        return node.has(fieldName) ? node.get(fieldName).asText() : null;
    }
    
    public static Boolean jsonNodeToBoolean(JsonNode node, String... fieldName)
    {
        if(node == null || fieldName == null || fieldName.length == 0) {
            return null;
        }
        
        if(fieldName.length == 1) {
            return node.has(fieldName[0]) ? node.get(fieldName[0]).asBoolean() : null;
        }
        else if(node.has(fieldName[0])) {
            JsonNode tmp = node.get(fieldName[0]);
            
            for(int i = 1; i < fieldName.length; i++) {
                if(tmp == null) {
                    return null;
                }
                
                if(tmp.has(fieldName[i])) {
                    tmp = tmp.get(fieldName[i]);
                }
            }
            
            return tmp != null ? tmp.asBoolean() : null;
        }
        
        return null;
        
//        return node.has(fieldName) ? node.get(fieldName).asText() : null;
    }
    
    public static Long jsonNodeToLong(JsonNode node, String fieldName)
    {
        if(node == null) {
            return null;
        }
        return node.has(fieldName) ? node.get(fieldName).asLong() : null;
    }
}
