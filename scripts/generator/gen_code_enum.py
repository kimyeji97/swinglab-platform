#!/usr/bin/python3
# -*- coding: utf-8 -*-

import sys , os, io, re
from unicodedata import name
#import mysql.connector
import psycopg2

dir_path = os.path.dirname(os.path.realpath(__file__))
sys.path.append(dir_path)

__IS_VERSION_3__= sys.version_info.major == 3

from gen_config import ENUM_TYPE_INTERFACE_PACKAGE, con_opts

use_surfinn = True


#where_sql = "WHERE cd_pid IS NULL and 1000 <= cd_id and cd_id <= 9000 order by cd_id"
where_sql = "WHERE cd_id <= 9999 order by cd_id"

class CodeGroup:
    def __init__(self, **kwargs):
        # print (kwargs)
        self.gcode    = kwargs['cd_id']
        self.gname    = kwargs['cd_nm']
        self.gname_disp = "" if kwargs['cd_disp_nm'] == "None" else kwargs['cd_disp_nm']
        self.gname_disp_eng = "" if kwargs['cd_disp_nm_eng'] == "None" else kwargs['cd_disp_nm_eng']
        self.desc    = kwargs['dscrpt']
        self.genum_name = kwargs['src_nm'].replace(' ','').replace('-','').replace('/','')
        self.codes    = []
    def __str__(self):
        return self.gcode + ", " + self.gname + ", codes => [" + ', '.join(map(str,self.codes)) + "]"

class Code:
    def __init__(self, **kwargs):
        self.code    = kwargs['cd_id']
        self.pcode   = kwargs['cd_pid']
        self.name    = kwargs['cd_nm']
        self.name_disp = "" if kwargs['cd_disp_nm'] == "None" else kwargs['cd_disp_nm']
        self.name_disp_eng = "" if kwargs['cd_disp_nm_eng'] == "None" else kwargs['cd_disp_nm_eng']
        self.desc    = kwargs['dscrpt']
        self.enum_name = kwargs['src_nm'].replace(' ','').replace('-','').replace('/','')
        self.value   = self.get_value()
    def __str__(self):
        return self.code + ":" + self.name + "[" + self.enum_name + "]"

    def get_value(self):
        if self.name_disp != 'None':
            return self.name_disp
        else:
            return self.name

    def to_enum_name(self, val):
        if __IS_VERSION_3__:
            return "".join(x if x else '' for x in re.split('[-_/ ]',val))
        else:
            return "".join(x if x else '' for x in re.split('[-_/ ]',val))

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

def get_code_groups(connection_opts):
    cnx = psycopg2.connect(**connection_opts)
    cursor = cnx.cursor()
    #cursor.execute('SELECT * FROM tb_group_cd WHERE group_cd_pid IS NULL',False)
    cursor.execute('SELECT * FROM tb_common_cd {}'.format(where_sql),False)
    def to_lower(s):
        return s.lower()
    #col_names = map(to_lower,cursor.column_names)
    col_names = map(to_lower,[desc[0] for desc in cursor.description])
    if __IS_VERSION_3__:
        col_names = list(col_names)

    fetchedCursor = cursor.fetchall()
    rows = []
    for row in fetchedCursor:
        str_row = None
        if __IS_VERSION_3__:
            str_row = list(map(str, row))
        else:
            str_row = row
        map_row = dict(zip(col_names, str_row))

        row_obj = CodeGroup(**map_row)
        rows.append(row_obj)
    cursor.close()
    cnx.close()
    return rows

def get_codes(code_group , connection_opts):
    cnx = psycopg2.connect(**connection_opts)
    cursor = cnx.cursor()
    sql = "SELECT * FROM tb_common_cd WHERE cd_pid = " + code_group.gcode #+ " and cd_pid < 9000"
    #sql = "SELECT * FROM tb_common_cd"
    # print (sql)
    cursor.execute(sql ,False)
    def to_lower(s):
        return s.lower()
    col_names = map(to_lower,[desc[0] for desc in cursor.description])
    if __IS_VERSION_3__:
        col_names = list(col_names)
    
    fetchedCursor = cursor.fetchall()
    for row in fetchedCursor:
        print(row)
        str_row = None
        if __IS_VERSION_3__:
            str_row = list(map(str, row))
        else:
            str_row = row
        map_row = dict(zip(col_names, str_row))

        row_obj = Code(**map_row)
        code_group.codes.append(row_obj)
    cursor.close()
    cnx.close()


    
add_import = ""
def create_src_string():
    if use_surfinn is True:
        src_import = "import com.macrogen.core.types.CommonCode;"
        code_interface = ""
    else:
        src_import = """
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
"""
        code_interface = """
    @JsonFormat(shape = Shape.OBJECT)
    public interface CommonCode
    {
        default Long getPcode() {
            return null;
        }

        Long getCode();

        String getValue();
    }

"""

    import_prefix = """package com.macrogen;

import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
{src_import}
import com.macrogen.core.jackson.th.*;
{add_import}
""".format(src_import = src_import,add_import = add_import)

    src_prefix = """{import_prefix}
/**
* All codes for coupon system.
*
* This source is generated by generator.
*/
public class MacrogenCodes
{{
    {code_interface}
    public static class MacrogenCode implements CommonCode
    {{
        private Long pcode;
        private Long code;
        private String value;
        
        public Long getPcode()
        {{
            return pcode;
        }}

        public void setPcode(Long pcode)
        {{
            this.pcode = pcode;
        }}
        public Long getCode()
        {{
            return code;
        }}

        public void setCode(Long code)
        {{
            this.code = code;
        }}

        public String getValue()
        {{
            return value;
        }}

        public void setValue(String value)
        {{
            this.value = value;
        }}
    }}

    public static <T extends Enum<T> & CommonCode> T enumByCode(Class<T> type, Long code)
    {{
        if (code == null)
        {{
            return null;
        }}
        final EnumSet<T> values = EnumSet.allOf(type);
        for (T e : values)
        {{
            if (e.getCode().equals(code))
            {{
                return e;
            }}
        }}
        return null;
    }}

    public static <T extends Enum<T> & CommonCode> T enumByValue(Class<T> type, String value)
    {{
        if (StringUtils.isBlank(value))
        {{
            return null;
        }}
        final EnumSet<T> values = EnumSet.allOf(type);
        for (T e : values)
        {{
            if (e.getValue().equalsIgnoreCase(value))
            {{
                return e;
            }}
        }}
        return null;
    }}

    public static <T extends Enum<T> & CommonCode> String valueByCode(Class<T> type, Long code)
    {{
        T t = enumByCode(type, code);
        if (t == null)
        {{
            return null;
        }}
        return t.getValue();
    }}

    public static <T extends Enum<T> & CommonCode> Long codeByValue(Class<T> type, String value)
    {{
        T t = enumByValue(type, value);
        if (t == null)
        {{
            return null;
        }}
        return t.getCode();
    }}

    public static <T extends Enum<T> & CommonCode> String enumValuesToString(Class<T> type)
    {{
        StringBuilder sb = new StringBuilder();
        final EnumSet<T> values = EnumSet.allOf(type);
        sb.append("[");
        for (T e : values)
        {{
            sb.append(e.getValue()).append(",");
        }}
        sb.deleteCharAt(sb.lastIndexOf(",")).append("]");
        return sb.toString();
    }}

    // ###########################################################################
    // Generated Area
    // ###########################################################################
""".format(import_prefix = import_prefix,code_interface = code_interface)
        
    return src_prefix + src_contents + "}"

def write_file_core(path,  file_name, data):
    with open(os.path.join(path,file_name), 'w', encoding='utf-8') as f :
        f.write(data)

template = """    
    @JsonDeserialize(using = {cls_name}.class)
    public enum {ename} implements CommonCode{add_interface}
    {{
		// @formatter:off
        {fields}
        ;
		// @formatter:on

        public static final Long pcode = {pcode}L;
        private Long code;
        private String value;
        private String dispValue;
        
        private {ename}(Long c , String v, String dv) {{
            this.code = c;
            this.value = v;
            this.dispValue = dv;
        }}
        
        @Override
        public Long getPcode()
        {{
            return pcode;
        }}

        @Override
        public Long getCode()
        {{
            return code;
        }}

        @Override
        public String getValue()
        {{
            return StringUtils.isEmpty(dispValue) ? value : dispValue;
        }}
    }}"""

src_contents = ""

code_groups = get_code_groups(con_opts)
for cg in code_groups:
    get_codes(cg , con_opts)

    add_interface = ""
    if cg.genum_name in ENUM_TYPE_INTERFACE_PACKAGE:
        for package in ENUM_TYPE_INTERFACE_PACKAGE[cg.genum_name]:
            add_import += """
import {package};
""".format(package = package)
            add_interface += ", " + package.split('.')[-1]

    fields = ",\n\t\t".join(map(lambda c : c.enum_name + "(" + c.code + "L, \"" + c.name + "\", \"" + c.name_disp + "\")", cg.codes))
    cls_name = to_class_name(cg.genum_name) + 'Deserializer'
    src_contents = src_contents + template.format(
        ename = cg.genum_name,
        fields = fields, 
        cls_name = cls_name, 
        pcode = cg.gcode,
        add_interface = add_interface
    ) + "\n\n"


target_folder = 'com.macrogen'
to_path = os.path.realpath(os.path.join(dir_path, '../../../macrogen-core/src/main','java',target_folder.replace('.','/')))
file_nm = 'MacrogenCodes.java'

write_file_core(to_path,file_nm, create_src_string())
print('Success : Write file -> {}'.format(os.path.join(to_path, file_nm)))
# print (src_prefix, src_contents, src_suffix)


