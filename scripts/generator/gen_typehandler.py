#!/usr/bin/python3
# -*- coding: utf-8 -*-


import sys , os, io
import datetime

__IS_VERSION_3__= sys.version_info.major == 3

dir_path = os.path.dirname(os.path.realpath(__file__))
sys.path.append(dir_path)

from gen_config import FIELD_NAME_ENUM_TYPES, ENUM_PACKAGE

DATE_FORMAT="%Y%m%d.%H%M%S"
tmpfolder = datetime.datetime.strftime(datetime.datetime.now(),DATE_FORMAT)

main_path = os.path.realpath(os.path.join(dir_path, '../../../macrogen-core/src/main' ))
th_path = os.path.join(main_path, 'java', 'com','macrogen','core','mybatis','th')
deserializer_path = os.path.join(main_path, 'java', 'com','macrogen','core','jackson','th')

def to_field_name(value):
    def camelcase(): 
        yield str.lower
        while True:
            yield str.capitalize
    c = camelcase()
    if __IS_VERSION_3__:
        return "".join(c.__next__()(x) if x else '_' for x in value.split("_"))
    else:
        return "".join(c.next()(x) if x else '_' for x in value.split("_"))

def to_class_name(value):
    tmpval = to_field_name(value)
    return tmpval[0].upper() + tmpval[1:]


def generate_mybatis_type_handler():
    for key in FIELD_NAME_ENUM_TYPES:
        enum = FIELD_NAME_ENUM_TYPES[key]
        cls_name = to_class_name(enum) + 'TypeHandler'

        src = """package com.macrogen.core.mybatis.th;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import {enumpackage};
import {enumpackage}.{type_name};

public class {cls_name} extends BaseTypeHandler<{type_name}>
{{
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, {type_name} parameter, JdbcType jdbcType)
        throws SQLException
    {{
        ps.setLong(i, parameter.getCode());
    }}

    @Override
    public {type_name} getNullableResult(ResultSet rs, String columnName) throws SQLException
    {{
        Long code = rs.getLong(columnName);
        if (rs.wasNull())
        {{
            return null;
        }}
        return getEnum(code);
    }}

    @Override
    public {type_name} getNullableResult(ResultSet rs, int columnIndex) throws SQLException
    {{
        Long code = rs.getLong(columnIndex);
        if (rs.wasNull())
        {{
            return null;
        }}
        return getEnum(code);
    }}

    @Override
    public {type_name} getNullableResult(CallableStatement cs, int columnIndex) throws SQLException
    {{
        Long code = cs.getLong(columnIndex);
        if (cs.wasNull())
        {{
            return null;
        }}
        return getEnum(code);
    }}
    
    private {type_name} getEnum(Long code)
    {{
        return MacrogenCodes.enumByCode({type_name}.class, code);
    }}
}}
""".format(enumpackage = ENUM_PACKAGE, cls_name = cls_name, type_name = enum)
        #tmpdir = os.path.join('C:\\Temp','typehandler-' + tmpfolder , 'mybatis')

        #if not os.path.exists(tmpdir):
        #    print ('Generated sources will be saved into : {}'.format(tmpdir))
        #    os.makedirs(tmpdir)
        
        #with open(os.path.join(tmpdir,cls_name + '.java'), 'w') as f :
        #    f.write(src.replace(r'\n', '\r\n'))
        write_file_core(th_path,cls_name + '.java',src)

    print ('Mybatis Type handlers have been generated. Copy the sources and paste to source directory.')


def generate_jackson_de_and_serializer():
    def write_src(cname, data):
        tmpdir = os.path.join('C:\\Temp','typehandler-' + tmpfolder , 'jackson')
        
        if not os.path.exists(tmpdir):
            print ('Generated jackson sources will be saved into : {}'.format(tmpdir))
            os.makedirs(tmpdir)
        
        with open(os.path.join(tmpdir,cname + '.java'), 'w') as f :
            f.write(data.replace(r'\n', '\r\n'))

    for key in FIELD_NAME_ENUM_TYPES:
        enum = FIELD_NAME_ENUM_TYPES[key]
        de_cls_name = to_class_name(enum) + 'Deserializer'

        deserializer_src = """package com.macrogen.core.jackson.th;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.core.convert.converter.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.IOException;

import {enumpackage};
import {enumpackage}.MacrogenCode;
import {enumpackage}.{type_name};

public class {cls_name} extends JsonDeserializer<{type_name}> implements Converter<String,{type_name}>
{{
    @Override
    public {type_name} deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException
    {{
        if (jp.isExpectedStartObjectToken())
        {{
            MacrogenCode cd = jp.readValueAs(MacrogenCode.class);
            if (cd == null)
            {{
                return null;
            }}

            if (cd.getCode() != null)
            {{
                return MacrogenCodes.enumByCode({type_name}.class, cd.getCode());
            }}

            if (StringUtils.isNotEmpty(cd.getValue()))
            {{
                return MacrogenCodes.enumByValue({type_name}.class, cd.getValue());
            }} else
            {{
                // Invalid Data
                return null;
            }}
        }}
        else
        {{
            return convertFromString(jp.getValueAsString());
        }}
    }}

    private {type_name} convertFromString(String value)
    {{
        if (StringUtils.isEmpty(value))
        {{
            return null;
        }}
        {type_name} pr = MacrogenCodes.enumByValue({type_name}.class, value);
        if (pr == null)
        {{
            pr = MacrogenCodes.enumByCode({type_name}.class, NumberUtils.toLong(value, -1));
        }}

        return pr;
    }}

    @Override
    public Class<{type_name}> handledType()
    {{
        return {type_name}.class;
    }}

    // Converter Implementation
    @Override
    public {type_name} convert(String source)
    {{
        return convertFromString(source);
    }}
}}
""".format(enumpackage = ENUM_PACKAGE,cls_name = de_cls_name, type_name = enum)
        #write_src(de_cls_name,deserializer_src)
        write_file_core(deserializer_path,de_cls_name + '.java',deserializer_src)

    print ('Jackson Deserializer have been generated. Copy the sources and paste to source directory.')


def write_file_core(path,  file_name, data):
    with open(os.path.join(path,file_name), 'w') as f :
        f.write(data)

############################################
## main()
############################################
generate_mybatis_type_handler()
generate_jackson_de_and_serializer()
