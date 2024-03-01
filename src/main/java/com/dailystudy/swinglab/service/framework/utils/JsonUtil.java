package com.dailystudy.swinglab.service.framework.utils;

import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabInternalServerErrorException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** 
 * JsonUtil 클래스 
 * Json 변환에 관련된 유틸리티 
 */
@Slf4j
public class JsonUtil {
    /**
     * Object를 Json String으로 변경한다.
     *
     * @param obj 변경할 Object
     * @return Json String
     */
    public static String parseJsonObject(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultSerializerProvider sp = new DefaultSerializerProvider.Impl();
        sp.setNullValueSerializer(new NullSerializer());
        mapper.setSerializerProvider(sp);
        return mapper.writeValueAsString(obj);
    }
    
    /**
     * Object를 Json String으로 변경한다.
     *
     * @param obj 변경할 Object
     * @return Json String
     */
    public static String parseJsonObjectNull(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializerProvider(new DefaultSerializerProvider.Impl());
        return mapper.writeValueAsString(obj);
    }
    
    /**
     * Json string을 List<Map> 객체로 변경한다.
     *
     * @param str 변경할 Json String
     * @return List<Map>
     */
    public static List<Map<String, Object>> parseStringAsList(String str) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> dtlList = null;
        dtlList = mapper.readValue(str, new TypeReference<List<Map<String, Object>>>(){});
        return dtlList;
    }
    
    /**
     * Json string을 List<T> 객체로 변경한다.
     * @param <T>
     *
     * @param str 변경할 Json String
     * @return List<T>
     */
    public static <T> List<T> parseStringAsList(String str, Class<T> classType) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        List<T> dtlList = mapper.readValue(str, mapper.getTypeFactory().constructCollectionType(List.class, classType));
        return dtlList;
    }

    /**
     * Json string을 List<T> 객체로 변경한다.
     * 에러 발생시 로그만 찍고 빈 리스트 리턴
     *
     * @param str
     * @param classType
     * @return
     * @param <T>
     */
    public static <T> List<T> parseStringAsListNotThrow(String str, Class<T> classType)
    {
        if (StringUtils.isEmpty(str))
        {
            return new ArrayList<>();
        }

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(str, mapper.getTypeFactory().constructCollectionType(List.class, classType));
        } catch (IllegalArgumentException | IOException e)
        {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
       /**
     * Json string을 JsonNode 객체로 변경한다.
     *
     * @param str 변경할 Json String
     * @return JsonNode
     */
    public static Map<String, String> parseStringToMap(String str) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(str, Map.class);
    }
    
    /**
     * Json string을 JsonNode 객체로 변경한다.
     *
     * @param str 변경할 Json String
     * @return JsonNode
     */
    public static JsonNode parseString(String str) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(str, JsonNode.class);
    }
    /**
     * Object를 JsonNode 객체로 변경한다.
     *
     * @param obj 변경할 Object
     * @return JsonNode
     */
    public static JsonNode parseObject(Object obj) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(obj);
    }
    

    
    /**
     * Object를 선언된 Type으로 변경한다.
     *
     * @param obj 변경할 Object
     * @param classType 변경할 Type
     * @return <T>
     */
    public static <T> T convertObject(Object obj, Class<T> classType) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(obj, classType);
    }
    /**
     * Object를 선언된 Type으로 변경한다.
     *
     * @param obj 변경할 Object
     * @param classType 변경할 Type
     * @return <T>
     */
    public static <T> T convertObjectWithUnkown(Object obj, Class<T> classType) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(obj, classType);
    }
    
    /**
     * Object를 선언된 Type으로 변경한다.
     *
     * @param obj
     *            변경할 Object
     * @return <T>
     */
    public static <T> T convertObject(Object obj, TypeReference<T> typeRef) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(obj, typeRef);
    }
    
    /**
     * Object를 선언된 Type으로 변경한다.
     *
     * @param obj
     *            변경할 Object
     * @return <T>
     */
    public static <T> T convertObjectWithUnkown(Object obj, TypeReference<T> typeRef) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(obj, typeRef);
    }
    
    /**
     * Object를 MultiValueMap<String, String>으로 변경한다.
     *
     * @param obj 변경할 Object
     * @return MultiValueMap<String, String>
     */
    public static MultiValueMap<String, String> convertObjectMultiValueMap(Object obj) throws JsonParseException, JsonMappingException, IOException{
        MultiValueMap<String, String> multiValueMaps = new LinkedMultiValueMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> maps = mapper.convertValue(obj, new TypeReference<Map<String, String>>() {});
        multiValueMaps.setAll(maps);
        return multiValueMaps;
    }
    
    /**
     * JsonString을 선언된 Type으로 변경한다.
     *
     * @param str 변경할 JsonString
     * @param classType 변경할 Type
     * @return <T>
     */
    public static <T> T parseString(String str, Class<T> classType) throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(str, classType);
    }

    /**
     * Json string을 선언된 Type으로 변경한다.
     * 에러 발생시 로그만 찍고 빈 객체를 리턴한다.
     *
     * @param str
     * @param classType
     * @return
     * @param <T>
     */
    public static <T> T parseStringNotThrow(String str, Class<T> classType)
    {
        T emptyObj = null;
        try
        {
            emptyObj = classType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            log.error(e.getMessage(), e);
            throw new SwinglabInternalServerErrorException("알수 없는 오류가 발생했습니다.");
        }

        if (StringUtils.isEmpty(str))
        {
            return emptyObj;
        }
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(str, classType);
        } catch (IllegalArgumentException | IOException e)
        {
            log.error(e.getMessage());
            return emptyObj;
        }
    }


    //  public static <T> T parseString(JsonNode jsonNode, Class<T> classType) throws JsonParseException, JsonMappingException, IOException{
//      ObjectMapper mapper = new ObjectMapper();
//      return mapper.readValue(parseJsonObject(jsonNode), classType);
//  }
    
//  public static JsonNode parseXml(String xml) throws JsonParseException, JsonMappingException, IOException{
//      JSONObject jObject = XML.toJSONObject(xml);
//      ObjectMapper objMapper = new ObjectMapper();
//      objMapper.enable(SerializationFeature.INDENT_OUTPUT);
//      return objMapper.readValue(jObject.toString(), JsonNode.class);
//  }
    
    /**
     * JsonString을 Java Domain Class 로 변경한다.
     *
     * @param jsonStr 변경할 JsonString
     * @param className Java Domain Class 명  
     * @return Java Domain Class String
     */
    public static String parseToJavaObject(String jsonStr, String className) throws JsonParseException, JsonMappingException, IOException {
        return parseToJavaObject(jsonStr, className, "");
    }
    
    /**
     * JsonString을 Java Domain Class 로 변경한다.
     *
     * @param jsonStr 변경할 JsonString
     * @param className Java Domain Class 명  
     * @param packageName Java Domain Package 명  
     * @return Java Domain Class String
     */
    public static String parseToJavaObject(String jsonStr, String className, String packageName) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();       
        JsonNode rootNode = mapper.readValue(jsonStr, JsonNode.class);
        
        return parseToJavaObject(rootNode, className, packageName);
    }
    
    /**
     * Map을 Java Domain Class 로 변경한다.
     *
     * @param json 변경할 Map
     * @param className Java Domain Class 명  
     * @return Java Domain Class String
     */
    public static String parseToJavaObject(Map json, String className) throws JsonParseException, JsonMappingException, IOException {
        return parseToJavaObject(parseJsonObject(json), className, "");
    }
    
    /**
     * Map을 Java Domain Class 로 변경한다.
     *
     * @param json 변경할 JsonString
     * @param className Java Domain Class 명
     * @param packageName Java Domain Package 명  
     * @return Java Domain Class String
     */
    public static String parseToJavaObject(Map json, String className, String packageName) throws JsonParseException, JsonMappingException, IOException {
        return parseToJavaObject(parseJsonObject(json), className, packageName);
    }
    
    /**
     * JsonNode를 Java Domain Class 로 변경한다.
     *
     * @param rootNode 변경할 JsonNode
     * @param className Java Domain Class 명  
     * @param packageName Java Domain Package 명  
     * @return Java Domain Class String
     */
    public static String parseToJavaObject(JsonNode rootNode, String className, String packageName) throws JsonParseException, JsonMappingException, IOException {
        StringBuffer sb = new StringBuffer();
        StringBuffer subSb = new StringBuffer();
        
        if(packageName != null && !packageName.equals("")) {
            sb.append("package ").append(packageName).append(";\n");
            sb.append("\n");
        }
        
        sb.append("@Data\n");
        sb.append("public class ");
        sb.append(className);
        sb.append(" {\n");
        
        Iterator<String> iter = rootNode.fieldNames();
        
        Integer indent = 1;
        while(iter.hasNext()){
            String key = iter.next();
            if(rootNode.get(key).isBoolean()) {
                sb.append("\tBoolean ").append(key).append(";\n");
            }else if(rootNode.get(key).isDouble()) {
                sb.append("\tDouble ").append(key).append(";\n");
            }else if(rootNode.get(key).isInt()) {
                sb.append("\tInteger ").append(key).append(";\n");
            }else if(rootNode.get(key).isTextual()) {
                sb.append("\tString ").append(key).append(";\n");
            }else if(rootNode.get(key).isLong()) {
                sb.append("\tLong ").append(key).append(";\n");
            }else if(rootNode.get(key).isObject()){
                String innerClassName = key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
                sb.append("\t").append(innerClassName).append(" ").append(key).append(";\n");
                subSb.append("\n");
                subSb.append("\t@Data\n");
                subSb.append("\tpublic static class ").append(innerClassName).append(" {\n");
                
                objectTypeMapping(rootNode.get(key), subSb, indent++);
                indent--;
                subSb.append("\t}\n");
            }else if(rootNode.get(key).isArray()){
                String innerClassName = key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
                sb.append("\t").append(innerClassName).append(" ").append(key).append(";\n");
                subSb.append("\n");
                subSb.append("\t@Data\n");
                subSb.append("\tpublic static class ").append(innerClassName).append(" {\n");
                
                objectTypeMapping(rootNode.get(key).get(0), subSb, indent++);
                indent--;
                subSb.append("\t}\n");
            }
        }
        
        if(subSb.length() > 0) {
            sb.append(subSb.toString());
        }
        
        sb.append("}");
        
        return sb.toString();
    }
        
    private static void objectTypeMapping(JsonNode cellNode, StringBuffer sb, Integer indent){
        Iterator<String> iter = cellNode.fieldNames();
        StringBuffer subSb = new StringBuffer();

        Integer cIndent = indent;
        while(iter.hasNext()){
            String key = iter.next();
            
            addIntent(sb, cIndent);
            
            if(cellNode.get(key).isBoolean()) {
                sb.append("Boolean ").append(key).append(";\n");
            }else if(cellNode.get(key).isDouble()) {
                sb.append("Double ").append(key).append(";\n");
            }else if(cellNode.get(key).isInt()) {
                sb.append("Integer ").append(key).append(";\n");
            }else if(cellNode.get(key).isTextual()) {
                sb.append("String ").append(key).append(";\n");
            }else if(cellNode.get(key).isLong()) {
                sb.append("Long ").append(key).append(";\n");
            }else if(cellNode.get(key).isObject()){
                String className = key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
                
                sb.append(className).append(" ").append(key).append(";\n");
                subSb.append("\n");             
                
                addIntent(subSb, cIndent);
                subSb.append("@Data\n");
                addIntent(subSb, cIndent);
                subSb.append("public static class ").append(className).append(" {\n");
                
                objectTypeMapping(cellNode.get(key), subSb, ++cIndent);
                
                addIntent(subSb, cIndent-1);
                
                subSb.append("}\n");
                
                --cIndent;
            }else if(cellNode.get(key).isArray()) {
                String className = key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
                                
                if(cellNode.get(key).size() > 0 && cellNode.get(key).get(0).isObject()) {
                    addIntent(subSb, cIndent);
                    sb.append("List<").append(className).append("> ").append(key).append(";\n");
                    
                    subSb.append("\n");             
                    addIntent(subSb, cIndent);
                    subSb.append("@Data\n");
                    addIntent(subSb, cIndent);
                    subSb.append("public static class ").append(className).append(" {\n");
                    
                    objectTypeMapping(cellNode.get(key).get(0), subSb, ++cIndent);
                    addIntent(subSb, cIndent-1);
                    subSb.append("}\n");                    
                }else {                 
                    JsonNode arrayChildNode = cellNode.get(key).get(0);
                    
                    if(arrayChildNode != null) {
                        if(arrayChildNode.isBoolean()) {
                            sb.append("List<Boolean> ").append(key).append(";\n");
                        }else if(arrayChildNode.isDouble()) {
                            sb.append("List<Double> ").append(key).append(";\n");
                        }else if(arrayChildNode.isInt()) {
                            sb.append("List<Integer> ").append(key).append(";\n");
                        }else if(arrayChildNode.isTextual()) {
                            sb.append("List<String> ").append(key).append(";\n");
                        }else if(arrayChildNode.isLong()) {
                            sb.append("List<Long> ").append(key).append(";\n");
                        }
                    }
                }
                
                --cIndent;
            }
        }
        
        if(subSb.length() > 0) {
            sb.append(subSb.toString());
        }
        
        --cIndent;
    }
    
    private static void addIntent(StringBuffer sb , Integer indent) {
        for(int i=0; i<=indent; i++) {
            sb.append("\t");
        }
    }

    /** 
     * NullSerializer 클래스 
     * Json 변환 시 null일경우 빈 문자열로 치환  
     */ 
    private static class NullSerializer extends JsonSerializer<Object> {
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString("");
        }
    }
}
