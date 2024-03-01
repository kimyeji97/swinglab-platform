#!/usr/bin/python
# -*- coding: utf-8 -*-

"""

It is supported for maping between tables and package structure.
Also It generates mybatis xml, mapper and model object for ttbb coupon system.

@author : Gwanggeun Yoo
"""

import sys , os, io ,re
import datetime


arguments = sys.argv
print(arguments)
list_target = ['domain','mapper','xml']
gen_targets = []
category_targets = []
all_targets = True

for gt in arguments[1:]:
    if gt.startswith("-C"):
        all_targets = False
        category_targets.append(gt[2:])
    else :
        gen_targets.append(gt)


__IS_VERSION_3__= sys.version_info.major == 3
_IS_WINDOW_ = os.name == 'Windows' or os.name == 'nt'

dir_path = os.path.dirname(os.path.realpath(__file__))
sys.path.append(dir_path)

main_path = os.path.realpath(os.path.join(dir_path, '../../src/main' ))
xml_path = os.path.join(main_path, 'resources', 'mybatis','gen')

main_path = os.path.realpath(os.path.join(dir_path, '../../src/main' ))
mapper_path =os.path.join(main_path, 'java', 'com','dailystudy','swinglab','service','framework','gen','mapper')

main_path = os.path.realpath(os.path.join(dir_path, '../../src/main' ))
model_path =os.path.join(main_path, 'java', 'com','dailystudy','swinglab','service','framework','gen','domain')



is_remove_cd = False # _CD 제거 여부
is_remove_yn = False # _YN 제거 여부
is_use_date_format = False # json 날짜포맷 사용 여부
regist_column_nm = 'REG_DT'
register_column_nm = 'REG_ID'
update_column_nm = 'UPD_DT'
updater_column_nm = 'UPD_ID'

app_id_column_nm = 'APP_ID'
nation_cd_column_nm = 'NATION_CD'
lang_cd_column_nm = 'LANG_CD'

base_domain_colums = [regist_column_nm,register_column_nm,update_column_nm,updater_column_nm, app_id_column_nm, nation_cd_column_nm, lang_cd_column_nm] # BaseDomain으로 처리할 컬럼 리스트
gen_domain_package = "com.dailystudy.swinglab.service.framework.gen.domain"




from gen_config import FIELD_NAME_ENUM_TYPES, ENUM_PACKAGE, con_opts, con_schema

def rreplace(s, old, new, occurrence):
    li = s.rsplit(old, occurrence)
    return new.join(li)

SP4  = ' '*4
SP8  = ' '*8
SP12 = ' '*12

def to_field_name(value):
    value = value.replace("tb_", "", 1)
    value = value.replace("TB_", "", 1)
    def camelcase():
        yield str.lower
        while True:
            yield str.capitalize
    c = camelcase()
    if __IS_VERSION_3__:
        return "".join(c.__next__()(x) if x else '_' for x in re.split('[-_ ]',value))
    else:
        return "".join(c.next()(x) if x else '_' for x in re.split('[-_ ]',value))

def bool_str(boo):
    if boo : return 'true'
    else : return 'false'

def to_class_name(value):
    tmpval = to_field_name(value)
    return tmpval[0].upper() + tmpval[1:]

def get_model_code(table,fields , mapper_package, model_gen_package):
    tname = table.table_name
    table_class_name = to_class_name(tname) + 'Core'
    # table_field_name = to_field_name(tname)
    source_prefix = []
    source_prefix.append("package {};".format(model_gen_package))
    source_prefix.append("")
    source_prefix.append("import com.dailystudy.swinglab.service.framework.domain.mybatis.Column;")
    source_prefix.append("import com.dailystudy.swinglab.service.framework.domain.jpa.entity.BaseEntity;")
    source_prefix.append("import lombok.ToString;")

    source = []
    source.append("@ToString")
    source.append("public class {} extends BaseDomain".format(table_class_name))
    source.append("{")
    for field in fields:
        f = field.name

        #if f == 'REG_DT' or f == 'REGR_ID' or f == 'REG_ID' or f == 'UPD_DT' or f == 'UPDR_ID' or f == 'UPD_ID':
        if f in base_domain_colums:
            continue

        if field.java_type_package is not None:
            import_str = "import " + field.java_type_package + ";"
            if import_str not in source_prefix:
                source_prefix.append(import_str)
        if field.jackson_prop is not None:
            imp_str = "import com.fasterxml.jackson.annotation.JsonProperty;"
            if imp_str not in source_prefix:
                source_prefix.append(imp_str)
            prop_str = []
            if 'value' in field.jackson_prop:
                prop_str.append('value = "{}"'.format(field.jackson_prop['value']))
            if 'access' in field.jackson_prop:
                prop_str.append('access = {}'.format(field.jackson_prop['access']))
            source.append('    @JsonProperty({})'.format(','.join(prop_str) ) )

        if(is_use_date_format):
            if field.type.startswith("date") or field.type.startswith("timestamp"):
                imp_str = "import org.springframework.format.annotation.DateTimeFormat;"
                if imp_str not in source_prefix:
                    source_prefix.append(imp_str)
                imp_str = "import com.fasterxml.jackson.annotation.JsonFormat;"
                if imp_str not in source_prefix:
                    source_prefix.append(imp_str)

            if field.type.startswith("datetime(3") or field.type.startswith("timestamp(3"):
                source.append('    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")')
                source.append('    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")')
            elif field.type == 'date':
                source.append('    @DateTimeFormat(pattern="yyyy-MM-dd")')
                source.append('    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")')
            elif field.type == 'datetime'or field.type == 'timestamp':
                source.append('    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")')
                source.append('    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")')

        # source.append('    @Column(columnName = "{}", type = "{}", length = {}, nullable = {})'.format(f, field.type, f.))
        source.append('    @Column(columnName = "{}", type = "{}", nullable = {}, primary = {})'.format(f, field.type, bool_str(field.nullable), bool_str(field.is_pk)))
        source.append("    private {} {};".format(field.java_type, field.java_field_name))

    for field in fields:
        f = field.name
        #if f == 'REG_DT' or f == 'REGR_ID' or f == 'REG_ID' or f == 'UPD_DT' or f == 'UPDR_ID' or f == 'UPD_ID':
        if f in base_domain_colums:
            continue
        field_t_name = field.java_field_name[0].upper() + field.java_field_name[1:]
        field_f_name = field.java_field_name
        java_type = field.java_type
        get_set_format = """
    public void set{field_t_name}({java_type} {field_f_name})
    {{
        this.{field_f_name} = {field_f_name};
    }}

    public {java_type} get{field_t_name}()
    {{
        return this.{field_f_name};
    }}"""

        # java_type이 배열이면 get,set clone해서.
        if java_type.endswith('[]') :
            get_set_format = """
    public void set{field_t_name}({java_type} {field_f_name})
    {{
    	if({field_f_name} != null)
    	{{
    		this.{field_f_name} = {field_f_name}.clone();
    	}}
    }}

    public {java_type} get{field_t_name}()
    {{
    	if(this.{field_f_name} != null)
    	{{
    		return this.{field_f_name}.clone();
    	}}
    	else
    	{{
    		return null;
    	}}
    }}"""

        get_set = get_set_format.format(field_t_name = field_t_name
                                        ,field_f_name = field_f_name
                                        ,java_type = java_type)
        source.append(get_set)
    source.append("}")

    source_prefix.append("")
    source_prefix.append("")
    return "\n".join(source_prefix + source)


def get_ex_model_code(table,fields , mapper_package, model_package):
    tname = table.table_name
    core_table_class_name = to_class_name(tname) + 'Core'
    table_class_name = to_class_name(tname)
    # table_field_name = to_field_name(tname)
    source_prefix = []
    source_prefix.append("package {};".format(model_package))
    source_prefix.append("")
    # source_prefix.append("import org.apache.commons.lang3.builder.ToStringBuilder;")
    source_prefix.append("import com.dailystudy.swinglab.service.framework.gen.domain." + core_table_class_name  + ";")
    source_prefix.append("import lombok.Data;")
    source_prefix.append("import lombok.EqualsAndHashCode;")

    source = []
    source.append("@Data")
    source.append("@EqualsAndHashCode(callSuper=false)")
    source.append("public class {} extends {}".format(table_class_name, core_table_class_name))
    source.append("{")
    source.append("}")

    source_prefix.append("")
    source_prefix.append("")
    return "\n".join(source_prefix + source)

def get_mapper_code(table, fields, mapper_package, model_package):
    tname = table.table_name
    table_class_name = to_class_name(tname)
    corePackage = "com.dailystudy.swinglab.service.framework.gen.mapper"

    mapper = """package %(mapper_package)s;

import %(gen_package)s.%(table_class_name)sMapperCore;
import %(model_package)s.%(table_class_name)s;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface %(table_class_name)sMapper extends %(table_class_name)sMapperCore<%(table_class_name)s>
{
    
}
"""
    return mapper % {
        'table_class_name' : table_class_name
        ,'mapper_package':mapper_package
        ,'model_package':model_package, 'gen_package':corePackage}


def gen_mapper_gen_code(table,fields , mapper_package, model_package):
    tname = table.table_name
    table_class_name = to_class_name(tname)
    table_field_name = to_field_name(tname)
    key_params = makeMapperKeyParams(fields)
    # print (key_params)
    corePackage = "com.dailystudy.swinglab.service.framework.gen.mapper"

    sequence_mapper_src = ''
    if table.sequence is not None:
        seq_cls_name = to_class_name(table.sequence)
        sequence_mapper_src = """
    Long getNext{seq_cls_name}();

    Long getLast{seq_cls_name}();

    Long setLast{seq_cls_name}(Long lastValue);
""".format(seq_cls_name = seq_cls_name
           ,sequence_name = table.sequence)

    mapper =  """package %(mapper_package)s;

import java.util.List;
%(key_params_import)s

public interface %(table_class_name)sMapperCore<T>
{
    // ########### START Gnerated Area ###################
    // {
    int create%(table_class_name)s(T %(table_field_name)s);
    
    T create%(table_class_name)sReturn(T %(table_field_name)s);

    int create%(table_class_name)sList(List<T> %(table_field_name)sList);

    T read%(table_class_name)s(T %(table_field_name)s); 

    T read%(table_class_name)sByKey(%(key_params)s);

    List<T> list%(table_class_name)s(T %(table_field_name)s);

    int update%(table_class_name)s(T %(table_field_name)s);

    T update%(table_class_name)sReturn(T %(table_field_name)s);

    int updateForce(T %(table_field_name)s);

    T updateForceReturn(T %(table_field_name)s);

    int delete%(table_class_name)s(T %(table_field_name)s);

    int delete%(table_class_name)sByKey(%(key_params)s);
%(sequence_mapper_src)s
    // }
    // ########### END Gnerated Area #####################

    // ###########################################################
    // ##
    // ## Please fill out the new methods below.
    // ##
    // ###########################################################
}
"""
    return mapper % {'table_class_name' : table_class_name
        ,'table_field_name' : table_field_name
        ,'mapper_package':corePackage
        ,'model_package':model_package
        ,'sequence_mapper_src' : sequence_mapper_src
        ,'key_params' : key_params['params_str']
        ,'key_params_import' : key_params['import_str']
     }

def makeWhere(fields) :
    xml = []
    javaFieldList = ['uri', 'host', 'method']
    for field in fields:
        f = field.name
        if f == regist_column_nm or f == update_column_nm:
            continue

        javaField = field.java_field_name
        null_check_string = field.null_check_string
        oper = '='
        if (javaField in javaFieldList ) or f.endswith("NM") or f.endswith("NAME"):
        # if f.endswith("NM") or f.endswith("NAME"):
            oper = 'ILIKE'

        xml.append("<if test=\"{}\">".format(null_check_string))
        xml.append("    AND {} {} #{{{}}}".format(f,oper,javaField))
#        if oper == 'ILIKE':
#        	xml.append("    AND {} {} '%' || #{{{}}} || '%'".format(f,oper,javaField))
#        else: 
#        	xml.append("    AND {} {} #{{{}}}".format(f,oper,javaField))
        xml.append("</if>")
    return SP8 + ("\n" + SP8).join(xml)


def makeMapperKeyParams(fields):
    imports = []
    params = []
    for field in fields:
        f = field.name
        java_type = field.java_type
        field_f_name = field.java_field_name
        if f == regist_column_nm or f == update_column_nm or field.is_auto_increment() or field.is_pk == False:
            continue

        params.append("@Param(\"{}\") {} {},".format(field_f_name,java_type,field_f_name))
        if field.java_type_package :
            imports.append("import {};".format(field.java_type_package))

    if len(params) > 0:
        imports.append("import org.apache.ibatis.annotations.Param;")

    params[-1] = params[-1][0:-1]
    return {
        'params_str' : (" ").join(params)
        ,'import_str' : ("\n").join(imports)
    }


def makeWherePk(table) :
    fields = table.fields
    xml = []
    for field in fields:
        f = field.name
        if f == regist_column_nm or f == update_column_nm or field.is_pk == False:
            continue

        javaField = field.java_field_name
        xml.append("AND {} = #{{{}}}".format(f,javaField))
    return SP12 + ("\n" + SP12).join(xml)


def makeColumns(table,fields,with_alias, only_pk) :
    alias = ""
    if with_alias:
        alias = table.table_alias + "."
        # tns = table.table_name.split("_")
        # for t in tns:
        #     alias += t[0].upper()
        # alias += "."

    xml = []
    for field in fields:
        f = field.name
        if f == regist_column_nm or f == update_column_nm or field.is_auto_increment():
            continue
        if only_pk == True and field.is_pk == False:
            continue
        xml.append("    {}{},".format(alias,f))

    # xml = xml.gsub(/\,$/,'')
    if not only_pk:
        if table.has_created_time():
            xml.append("    {}{},".format(alias,regist_column_nm))
        if table.has_updated_time():
            xml.append("    {}{},".format(alias,update_column_nm))
    xml[-1] = xml[-1][0:-1]
    return SP8 + ("\n" + SP8).join(xml)

def makeValues(table,fields) :
    xml = []
    for field in fields:
        f = field.name
        if f == regist_column_nm or f == update_column_nm or field.is_auto_increment():
            continue
        # print (field.default)
        if field.sequence_name:
            xml.append("    COALESCE(#{{{}}},NEXTVAL('{}')),".format(field.java_field_name,field.sequence_name))
        elif field.nullable == False and field.default:
            xml.append("    COALESCE(#{{{}}},{}),".format(field.java_field_name,field.default))
        elif f == 'DEL_YN':
            xml.append("    COALESCE(#{{{}}},FALSE),".format(field.java_field_name))
        else:
            xml.append("    #{{{}}},".format(field.java_field_name))

    # xml = xml.gsub(/\,$/,'')
    if table.has_created_time():
        xml.append("    COALESCE(#{regDt},CURRENT_TIMESTAMP),")
    if table.has_updated_time():
        xml.append("    COALESCE(#{updDt},CURRENT_TIMESTAMP),")
    xml[-1] = xml[-1][0:-1]
    return SP8 + ("\n" + SP8).join(xml)

def makeValuesForeach(table, fields):
    tname = table.table_name
    table_field_name = to_field_name(tname)
    xml = []
    xml.append("    <foreach collection=\"list\" item=\"item\" separator=\",\">".format(table_field_name))
    xml.append("    (")
    for field in fields:
        f = field.name
        if f == regist_column_nm or f == update_column_nm or field.is_auto_increment():
            continue
        if field.sequence_name:
            xml.append("        COALESCE(#{{item.{}}},NEXTVAL('{}')),".format(field.java_field_name,field.sequence_name))
        elif field.nullable == False and field.default:
            xml.append("        COALESCE(#{{item.{}}},{}),".format(field.java_field_name,field.default))
        elif f == 'DEL_YN':
            xml.append("        COALESCE(#{{item.{}}},FALSE),".format(field.java_field_name))
        else:
            xml.append("        #{{item.{}}},".format(field.java_field_name))

    # xml = xml.gsub(/\,$/,'')
    if table.has_created_time():
        xml.append("        COALESCE(#{item.regDt},CURRENT_TIMESTAMP),")
    if table.has_updated_time():
        xml.append("        COALESCE(#{item.updDt},CURRENT_TIMESTAMP),")
    xml[-1] = xml[-1][0:-1]
    xml.append("    )")
    xml.append("    </foreach>")
    return SP8 + ("\n" + SP8).join(xml)


def makeInsert(table,fields) :
    table_name = table.table_name.upper()
    xml = []
    xml.append("INSERT INTO {} (".format(table_name))
    for field in fields:
        f = field.name
        if f == regist_column_nm or f == update_column_nm or field.is_auto_increment():
            continue
        xml.append("    {},".format(f))

    # xml = xml.gsub(/\,$/,'')
    if table.has_created_time():
        xml.append("    {},".format(regist_column_nm))
    if table.has_updated_time():
        xml.append("    {},".format(update_column_nm))
    xml[-1] = xml[-1][0:-1]
    xml.append(")")
    xml.append("VALUES (")

    for field in fields:
        f = field.name
        if f == regist_column_nm or f == update_column_nm or field.is_auto_increment():
            continue
        if field.sequence_name:
            xml.append("    COALESCE(#{{{}}},NEXTVAL('{}')),".format(field.java_field_name,field.sequence_name))
        elif field.nullable == False and field.default:
            xml.append("    COALESCE(#{{{}}},{}),".format(field.java_field_name,field.default))
        elif f == 'DEL_YN':
            xml.append("    COALESCE(#{{{}}},FALSE),".format(field.java_field_name))
        else:
            xml.append("    #{{{}}},".format(field.java_field_name))

    # xml = xml.gsub(/\,$/,'')
    if table.has_created_time():
        xml.append("    COALESCE(#{regDt},CURRENT_TIMESTAMP),")
    if table.has_updated_time():
        xml.append("    COALESCE(#{updDt},CURRENT_TIMESTAMP),")
    xml[-1] = xml[-1][0:-1]
    xml.append("    )")
    return SP8 + ("\n" + SP8).join(xml)


def makeUpdateSelective(table, fields) :
    table_name = table.table_name.upper()
    xml = []
    xml.append("UPDATE {} ".format(table_name))
    xml.append("<set>")
    for field in fields:
        f = field.name
        if f == regist_column_nm or f == update_column_nm or field.is_pk or field.is_auto_increment():
            continue
        # null_check_string = field.null_check_string
        javaField = field.java_field_name
        xml.append("    <if test=\"{} != null\">".format(javaField))
        xml.append("        {} = #{{{}}},".format(f, javaField))
        xml.append("    </if>")
    if table.has_updated_time():
        xml.append("    {} = CURRENT_TIMESTAMP".format(update_column_nm))
    xml.append("</set>")

    return SP8 + ("\n" + SP8).join(xml)

def makeUpdate(table, fields) :
    table_name = table.table_name.upper()
    xml = []
    xml.append("UPDATE {} ".format(table_name))
    xml.append("<set>")
    for field in fields:
        f = field.name
        if f == regist_column_nm or f == register_column_nm or f == update_column_nm or field.is_pk or field.is_auto_increment():
            continue
        # null_check_string = field.null_check_string
        javaField = field.java_field_name
        xml.append("    {} = #{{{}}},".format(f, javaField))
    if table.has_updated_time():
        xml.append("    {} = CURRENT_TIMESTAMP".format(update_column_nm))
    xml.append("</set>")

    return SP8 + ("\n" + SP8).join(xml)

def makeUpdateWhere(table) :
    fields = table.fields
    xml = []
    is_first = True
    for field in fields:
        f = field.name
        if f == regist_column_nm or f == update_column_nm or field.is_pk == False:
            continue

        javaField = field.java_field_name
        if is_first:
            xml.append("AND {} = #{{{}}}".format(f,javaField))
            is_first = False
        else:
            null_check_string = field.null_check_string
            xml.append("<if test=\"{}\">".format(null_check_string))
            xml.append("    AND {} = #{{{}}}".format(f,javaField))
            xml.append("</if>")
    return SP12 + ("\n" + SP12).join(xml)


def makeSelectDefault(table,fields) :
    table_name = table.table_name.upper()
    field_str = ""
    for field in fields:
        f = field.name
        field_name = f.upper()
        if field_name == regist_column_nm or field_name == update_column_nm:
            continue
        field_str = field_str + "\n            {},".format(field_name)
    if table.has_created_time():
        field_str = field_str + "\n            {},".format(regist_column_nm)
    if table.has_updated_time():
        field_str = field_str + "\n            {},".format(update_column_nm)
    field_str = field_str[0:-1]

    defaultxml = """
        SELECT  {field_str}
        FROM {table_name}
    """ .format (field_str = field_str , table_name = table_name)
    return defaultxml



def make_internal_xml_file(table, fields) :
    tname = table.table_name.upper()
    tname_alias = table.table_alias.upper()
    table_ns = table.table_namespace.upper()
    where_if = makeWhere(fields)
    where_pk_if = makeWherePk(table)
    columns_sql = makeColumns(table, fields, False, False)
    columns_alias_sql = makeColumns(table, fields, True, False)
    pks_sql = makeColumns(table, fields, False, True)
    pks_alias_sql = makeColumns(table, fields, True, True)
    values_sql = makeValues(table, fields)
    values_foreach_sql = makeValuesForeach(table, fields)
    # select_default = makeSelectDefault(table, fields)
    # insert_sql = makeInsert(table, fields)
    update_set = makeUpdateSelective(table, fields)
    update_set_force = makeUpdate(table, fields)
    update_where = makeUpdateWhere(table)

    return """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="{table_ns}">

    <sql id="ifConditionSql">
{where_if}
    </sql>
    
    <sql id="pkConditionSql">
{where_pk_if}
    </sql>

    <sql id="columnsSql">
{columns_sql}
    </sql>

    <sql id="columnsWithAliasSql">
{columns_alias_sql}
    </sql>
    
    <sql id="pkColumnsSql">
{pks_sql}
    </sql>
    
    <sql id="pkColumnsWithAliasSql">
{pks_alias_sql}
    </sql>

    <sql id="valuesSql">
{values_sql}
    </sql>

    <sql id="valuesForeachSql">
{values_foreach_sql}
    </sql>

    <sql id="selectFromSql">
        SELECT
            <include refid="{table_ns}.columnsSql" />
        FROM {tname}
    </sql>

    <sql id="selectFromWithAliasSql">
        SELECT
            <include refid="{table_ns}.columnsWithAliasSql" />
        FROM {tname} as {tname_alias}
    </sql>

    <sql id="whereSql">
        <where>
            <include refid="{table_ns}.ifConditionSql" />
        </where>
    </sql>

    <sql id="limitSql">
        <!-- <if test="size != null">
            limit #{{size}} 
            <if test="page != null ">
                offset (#{{size}}*(#{{page}}-1))
            </if>
           
        </if> -->
    </sql>
    
    <sql id="createSql">
        INSERT INTO {tname} (
            <include refid="{table_ns}.columnsSql" />
        )
        VALUES (
            <include refid="{table_ns}.valuesSql" />
        )
    </sql>
    
    <sql id="createListSql">
        INSERT INTO {tname} (
            <include refid="{table_ns}.columnsSql" />
        )
        VALUES
        <include refid="{table_ns}.valuesForeachSql" />
    </sql>

    <sql id="updateSelectiveSet">
{update_set}
    </sql>

    <sql id="updateSelectiveSql">
        <include refid="{table_ns}.updateSelectiveSet" />
        <where>
{update_where}
        </where>
    </sql>

    <sql id="updateForceSql" >
{update_set_force}
        <where>
            <include refid="{table_ns}.pkConditionSql" />
        </where>
    </sql>

    <sql id="deleteSql">
        DELETE FROM {tname}
        <where>
            <include refid="{table_ns}.ifConditionSql" />
        </where>
    </sql>
    
    <sql id="deleteByKeySql">
        DELETE FROM {tname}
        <where>
            <include refid="{table_ns}.pkConditionSql" />
        </where>
    </sql>

    <sql id="returningSql">
        RETURNING <include refid="{table_ns}.columnsSql" />
    </sql>
    
    <sql id="returningWithAliasSql">
        RETURNING <include refid="{table_ns}.columnsWithAliasSql" />
    </sql>
    
    <sql id="onConflictKeyDoUpdateSql">
        ON CONFLICT (
            <include refid="{table_ns}.pkColumnsSql" />
        ) DO UPDATE
    </sql>
    
    <sql id="onConflictKeyDoNothingSql">
        ON CONFLICT (
            <include refid="{table_ns}.pkColumnsSql" />
        ) DO NOTHING
    </sql>
</mapper>

""".format(table_ns = table_ns
           , where_if = where_if
           , where_pk_if = where_pk_if
           , columns_sql = columns_sql
           , columns_alias_sql = columns_alias_sql
           , pks_sql = pks_sql
           , pks_alias_sql = pks_alias_sql
           , values_sql = values_sql
           , values_foreach_sql = values_foreach_sql
           # ,select_default = select_default
           # ,insert_sql = insert_sql
           , update_set = update_set
           , update_set_force = update_set_force
           , update_where = update_where
           , tname  = tname
           , tname_alias = tname_alias)


def make_external_xml_file(table, fields , mapper_package , model_package) :
    table_ns = table.table_namespace.upper()
    table_class_name = to_class_name(table.table_name)

    sequence_xml = ''
    if table.sequence is not None:
        seq_cls_name = to_class_name(table.sequence)
        sequence_xml = """
    <select id="getNext{seq_cls_name}" resultType="java.lang.Long" useCache="false" flushCache="true">
        SELECT NEXTVAL('{sequence_name}')
    </select>
    <select id="getLast{seq_cls_name}" resultType="java.lang.Long" useCache="false" flushCache="true">
        SELECT CURRVAL('{sequence_name}')
    </select>
    <select id="setLast{seq_cls_name}" parameterType="java.lang.Long" resultType="java.lang.Long" useCache="false" flushCache="true">
        SELECT SETVAL('{sequence_name}', #{{_parameter}})
    </select>
""".format(seq_cls_name = seq_cls_name
           ,sequence_name = table.sequence)

    return """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="{mapper_package}.{table_class_name}Mapper">

    <!-- User can add specific conditions here for default select sql -->
    <sql id="additionalWhereSql">
    </sql>

    <sql id="selectSql">
        <include refid="{table_ns}.selectFromSql" />
        <where>
            <include refid="{table_ns}.ifConditionSql" />
            <include refid="{mapper_package}.{table_class_name}Mapper.additionalWhereSql" />
        </where>
        <include refid="{table_ns}.limitSql" />
    </sql>

    <select id="read{table_class_name}" parameterType="{model_package}.{table_class_name}" resultType="{model_package}.{table_class_name}">
        <include refid="selectSql" />
    </select>

    <select id="read{table_class_name}ByKey" resultType="{model_package}.{table_class_name}">
        <include refid="{table_ns}.selectFromSql" />
        <where>
            <include refid="{table_ns}.pkConditionSql" />
        </where>
    </select>

    <select id="list{table_class_name}" parameterType="{model_package}.{table_class_name}" resultType="{model_package}.{table_class_name}">
        <include refid="selectSql" />
    </select>
    
    <insert id="create{table_class_name}" parameterType="{model_package}.{table_class_name}">
        <include refid="{table_ns}.createSql" />
    </insert>
    
    <select id="create{table_class_name}Return" parameterType="{model_package}.{table_class_name}" resultType="{model_package}.{table_class_name}">
        <include refid="{table_ns}.createSql" />
        <include refid="{table_ns}.returningSql" />
    </select>

    <insert id="create{table_class_name}List" parameterType="java.util.List">
        <include refid="{table_ns}.createListSql" />
    </insert>

    <update id="update{table_class_name}" parameterType="{model_package}.{table_class_name}">
        <include refid="{table_ns}.updateSelectiveSql" />
    </update>

    <select id="update{table_class_name}Return" parameterType="{model_package}.{table_class_name}" resultType="{model_package}.{table_class_name}">
        <include refid="{table_ns}.updateSelectiveSql" />
        <include refid="{table_ns}.returningSql" />
    </select>

    <update id="updateForce" parameterType="{model_package}.{table_class_name}">
        <include refid="{table_ns}.updateForceSql" />
    </update>

    <select id="updateForceReturn" parameterType="{model_package}.{table_class_name}" resultType="{model_package}.{table_class_name}">
        <include refid="{table_ns}.updateForceSql" />
        <include refid="{table_ns}.returningSql" />
    </select>

    <delete id="delete{table_class_name}" parameterType="{model_package}.{table_class_name}">
        <include refid="{table_ns}.deleteSql" />
    </delete>

    <delete id="delete{table_class_name}ByKey">
        <include refid="{table_ns}.deleteByKeySql" />
    </delete>

    {sequence_xml}

    <!--
    #############################################################
    #
    # Please fill out the new SQLs below.
    #
    #############################################################
    -->
</mapper>

""".format(mapper_package = mapper_package
           ,model_package = model_package
           ,table_ns = table_ns
           ,table_class_name = table_class_name
           ,sequence_xml = sequence_xml)


class Table:
    def __init__(self, tname, fields, **kwargs):
        #if 'sequence' in kwargs and kwargs['sequence'] is not None:
        #    print ("Sequence :" , kwargs['sequence'])
        self.table_name = tname
        self.table_alias = self.get_table_alias(tname)
        self.table_namespace = 'TNS_' + tname
        self.fields = fields
        self.sequence = kwargs['sequence']
        self.kwargs = kwargs

    def has_updated_time(self):
        return self.has_column(update_column_nm.lower())

    def has_created_time(self):
        return self.has_column(regist_column_nm.lower())

    def has_column(self,column_name):
        for field in self.fields:
            if field.name.lower() == column_name:
                return True
        return False

    def get_mapper_name():
        return to_class_name(self.table_name) + "Mapper.java"
    def get_model_name():
        return to_class_name(self.table_name) + ".java"
    def get_xml_name():
        return to_class_name(self.table_name) + ".xml"
    def get_table_alias(self,table_name):
        x = table_name.split("_")
        alias = ""
        for t in x:
            alias += t[0].upper()
        return alias

class FieldAttr:
    def __init__(self, **kwargs):
        self.fieldName = kwargs['field_name']  if 'field_name' in kwargs else None
        self.javaType = kwargs['java_type'] if 'java_type' in kwargs else None
        self.jsonProperties = kwargs['json_props'] if 'json_props' in kwargs else None
        # YJ 시퀀스 작업
        self.sequenceName = kwargs['sequence_name'] if 'sequence_name' in kwargs else None

class TableField:
    def __init__(self, **kwargs):
        # print (kwargs)
        self.field_attrs    = kwargs['field_attrs']
        self.name              = kwargs['field'].upper()
        self.is_pk             = kwargs['key'] == 'PRI'
        self.nullable          = kwargs['null'] != 'NO'
        self.type              = kwargs['type']
        self.key               = kwargs['key']
        self.extra             = '' #kwargs['extra']
        # python3 으로 업그레이드 하면서 default 조회값에 null이 아니라 None 이라는 문자열로 값이 들어감.
        if isinstance(kwargs['default'], str) and kwargs['default'] == 'None':
            self.default = None
        else:
            self.default           = kwargs['default']
        self.java_type_package = None
        self.java_type         = self._mk_java_type()
        self.java_field_name   = self._mk_java_field_name()
        self.jackson_prop = self._mk_jackson_prop()
        self.null_check_string = self._mk_null_check_string()

        # YJ 시퀀스 작업
        self.sequence_name = self._mk_sequence_name()

        # print('key:{} , default : {} -> {}'.format(kwargs['key'], self.default, type(self.default)))

        # if self.default :
        #     if self.java_type == 'String':
        #         self.default = "'" + self.default + "'"
        # print(self.default)
    def is_auto_increment(self):
        return self.key == 'UNI' and self.extra == 'auto_increment'
    def _mk_sequence_name(self):
        if self.name in self.field_attrs and self.field_attrs[self.name].sequenceName:
            return self.field_attrs[self.name].sequenceName
        else:
            return None
    def _mk_java_field_name(self):
        if self.name not in self.field_attrs:
            return to_field_name(self.name)
        else:
            attrs = self.field_attrs[self.name]
            if attrs.fieldName:
                return self.field_attrs.fieldName
            else:
                return to_field_name(self.name)
    def _mk_java_type(self):
        type_name = self.type.lower()
        name = self.name.lower()
        if name in self.field_attrs and self.field_attrs[name].javaType:
            tmpJavaType = self.field_attrs[name].javaType

            dot_pos = tmpJavaType.rfind('.')
            if dot_pos == -1:
                return tmpJavaType
            else:
                self.java_type_package = tmpJavaType
                return tmpJavaType[dot_pos+1:]
        #if type_name.startswith("timestamp(") or type_name.startswith("datetime("):
        #    self.java_type_package = 'java.sql.Timestamp'
        #    return 'Timestamp'
        #el
        if type_name.startswith("timestamp"):
            self.java_type_package = 'java.util.Date'
            return 'Date'
        elif type_name.startswith("date"):
            self.java_type_package = 'java.util.Date'
            return 'Date'
        elif type_name.startswith("bigint") or type_name.startswith('serial') or type_name.startswith('int8'):
            if self.name in FIELD_NAME_ENUM_TYPES:
                self.java_type_package = ENUM_PACKAGE + "." + FIELD_NAME_ENUM_TYPES[self.name]
                return FIELD_NAME_ENUM_TYPES[self.name]
            else:
                return 'Long'
        elif type_name.startswith("interval"):
            return 'String'
        elif type_name.startswith("int"):
            if self.name in FIELD_NAME_ENUM_TYPES:
                self.java_type_package = ENUM_PACKAGE + "." + FIELD_NAME_ENUM_TYPES[self.name]
                return FIELD_NAME_ENUM_TYPES[self.name]
            else:
                return 'Integer'
        elif type_name.startswith("float") or type_name.startswith("double"):
            return 'Double'
        elif type_name.startswith("decimal(") or type_name.startswith("numeric") :
            self.java_type_package = 'java.math.BigDecimal'
            return "BigDecimal"
            # return "Double"
        elif type_name.startswith("tinyint(1)") or type_name.startswith("bool"):
            return 'Boolean'
        elif type_name.startswith("bytea") or type_name.startswith("blob"):
            return 'byte[]'
        elif type_name.startswith("_varchar"):
            self.java_type_package = 'java.util.List'
            return 'List<String>'
        else:
            if self.name in FIELD_NAME_ENUM_TYPES:
                self.java_type_package = ENUM_PACKAGE + "." + FIELD_NAME_ENUM_TYPES[self.name]
                return FIELD_NAME_ENUM_TYPES[self.name]
            else:
                return 'String'
            #return 'String'
    def _mk_null_check_string(self):
        javaField = self.java_field_name
        if self.java_type == 'String':
            return "{} != null and {}.length() > 0".format(javaField,javaField)
        else:
            return "{} != null".format(javaField)
    def _mk_jackson_prop(self):
        json_props = {}
        json_prop_name = None

        if is_remove_cd :
            if self.name.endswith('_CD') and self.java_type_package is not None:
                # return self.name[:-3].lower()
                json_prop_name = self.java_field_name[:-2]

        if is_remove_yn :
            if self.name.endswith('_YN') and self.java_type == 'Boolean':
                # return self.name[:-3].lower()
                json_prop_name = self.java_field_name[:-2]

        # config value.
        if json_prop_name:
            json_props['value'] = json_prop_name

        if self.field_attrs and self.name in self.field_attrs and self.field_attrs[self.name].jsonProperties :
            jsonProperties = self.field_attrs[self.name].jsonProperties
            for prop_key in jsonProperties:
                json_props[prop_key] = jsonProperties[prop_key]

        if len(json_props) == 0:
            return None
        else:
            return json_props

    def print_info(self):
        print ("{:30}\t{}\t{}\t{:12}\t{}".format(self.name, self.is_pk, self.nullable, self.type, self.java_type))


#import mysql.connector
import psycopg2

def get_tables(connection_opts,con_schema):
    cnx = psycopg2.connect(**connection_opts)
    cursor = cnx.cursor()
    #cursor.execute('show tables',False)
    cursor.execute("select table_name from information_schema.tables where table_schema='"+con_schema+"'",False)
    def to_lower(s):
        return s.lower()
    # col_names = map(to_lower,cursor.column_names)
    rows = []
    for row in cursor:
        # map_row = dict(zip(col_names, row))
        rows.append(row[0])
    cursor.close()
    cnx.close()
    return rows

def get_field_info(table_name , connection_opts, con_schema, field_attrs = {}):
    cnx = psycopg2.connect(**connection_opts)
    cursor = cnx.cursor()
    # cursor.execute('desc %(table_name)s',{'table_name':table_name},False)
    sql = """SELECT 
   c.column_name as Field
   , udt_name as Type
   , is_nullable as Null
   , b.key as Key
   , c.column_default  as Default
FROM 
   information_schema.columns as c
   left join (SELECT CC.COLUMN_NAME AS column_name,'PRI' as Key
	  FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS       TC
	      join INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE CC
	      on (TC.TABLE_CATALOG = CC.TABLE_CATALOG AND TC.TABLE_SCHEMA = CC.TABLE_SCHEMA AND TC.TABLE_NAME = CC.TABLE_NAME AND TC.CONSTRAINT_NAME = CC.CONSTRAINT_NAME)
	 WHERE 
	 	TC.TABLE_NAME = %(table_name)s AND TC.CONSTRAINT_TYPE = 'PRIMARY KEY'
	) as b on (c.column_name = b.column_name)
    JOIN pg_constraint p ON c.table_name = p.conrelid::regclass::text AND p.contype = 'p'
WHERE 
   table_schema = %(con_schema)s and table_name = %(table_name)s
order by array_position(p.conkey, c.ordinal_position)
"""

    cursor.execute(sql,{'table_name':table_name,'con_schema':con_schema})
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
        map_row['field_attrs'] = field_attrs
        field = TableField(**map_row)
        rows.append(field)
    cursor.close()
    cnx.close()
    return rows


_TEMP_DIR_ = 'C:\\Temp\generator' if _IS_WINDOW_ else os.path.join(os.path.expanduser("~"),"Temp","generator")
DATE_FORMAT="%Y%m%d.%H%M%S"
tmpfolder = datetime.datetime.strftime(datetime.datetime.now(),DATE_FORMAT)

print('Temp Directory', _TEMP_DIR_)

## 분리된 폴더와에 각각 mapper, xml , model 분리하여 저장하고 상위 폴더에 모든 파일을 한번 더 생성하여 준다.
##
def write_file(category, group, file_name, data):

    if category == None or len(category) == 0:
        tmpdir_all = os.path.join(_TEMP_DIR_,'mybatis-gen-' + tmpfolder)
        tmpdir = os.path.join(_TEMP_DIR_,'mybatis-gen-' + tmpfolder , group)
    else:
        tmpdir_all = os.path.join(_TEMP_DIR_,'mybatis-gen-' + tmpfolder , category)
        tmpdir = os.path.join(_TEMP_DIR_,'mybatis-gen-' + tmpfolder , category , group)

    if not os.path.exists(tmpdir):
        # print ('tmpdir to : {}'.format(tmpdir))
        os.makedirs(tmpdir)
    write_file_core(tmpdir, file_name , data)
    write_file_core(tmpdir_all, file_name, data)

    #with open(os.path.join(tmpdir,file_name), 'w') as f :
    #    f.write(data)
    #with open(os.path.join(tmpdir_all,file_name), 'w') as f :
    #    f.write(data)

def write_file_core(path,  file_name, data):
    with open(os.path.join(path,file_name), 'w') as f :
        f.write(data)

def generate_mybatis(table_name, category, mapper_package, model_package, sequence = None, field_attrs = {}):
    # print ("Sequence :" , sequence)

    if (sequence is not None):
        field = sequence.replace(table_name + "_", "", 1)
        field = rreplace(table_name, "_SEQ", "", 1);
        field = rreplace(table_name, "_seq", "", 1);
        # field = sequence.replace("_SEQ", "")
        # field = sequence.replace("_seq", "")
        field_attrs = {field : FieldAttr(sequence_name = sequence)}

    table_name = table_name.lower()
    db_fields = get_field_info( table_name, con_opts,con_schema , field_attrs)

    if(len(db_fields) < 1):
        print("\r\nFAIL !!! : {} ====> no colums\r\n".format(table_name))
        return

    table = Table(table_name.upper(),db_fields, sequence = sequence)

    tns = table.table_namespace

    ## write files to tmp directory.
    class_name = to_class_name(table_name)


    is_make_model = list_target[0] in gen_targets
    is_make_mapper = list_target[1] in gen_targets
    is_make_xml = list_target[2] in gen_targets

    # Results ...
    if len(gen_targets) == 0  or is_make_model:
        in_model_src = get_model_code(table,db_fields,mapper_package,gen_domain_package)
        ex_model_src = get_ex_model_code(table,db_fields,mapper_package,model_package)

        write_file_core(model_path , class_name + "Core.java"   , in_model_src)
        write_file('domain', category, class_name + ".java"       , ex_model_src)
        #write_file(None,     'model_in' , class_name + "Core.java"   , in_model_src) # model_path

    if len(gen_targets) == 0  or is_make_mapper:
        gen_mapper_src   = gen_mapper_gen_code(table,db_fields,mapper_package,model_package)
        mapper_src = get_mapper_code(table, db_fields, mapper_package, model_package)

        # write_file(category, 'mapper'   , class_name + "Mapper.java" , gen_mapper_src)
        write_file_core(mapper_path, class_name + "MapperCore.java", gen_mapper_src)
        write_file('mapper', category, class_name + "Mapper.java", mapper_src)

    if len(gen_targets) == 0  or is_make_xml:
        in_xml_src   = make_internal_xml_file(table,db_fields)
        ex_xml_src   = make_external_xml_file(table,db_fields,mapper_package,model_package)

        write_file_core(xml_path   , tns.upper() + ".xml"        , in_xml_src)
        write_file('xml', category, class_name + "Mapper.xml"        , ex_xml_src)
        #write_file(None,     'xml_in'   , tns        + ".xml"        , in_xml_src)  # xml_path


    print ("Generated : {}".format(table_name))


def generate_mybatis_files():

    #########################################
    mapper_base_pkg = "com.dailystudy.swinglab.service.framework.mapper"
    model_base_pkg = "com.dailystudy.swinglab.service.framework.domain"
    #########################################

    category = 'common'
    if all_targets or category in category_targets:
        mapper_package = mapper_base_pkg + "." + category
        model_package  = model_base_pkg + "." + category

        generate_mybatis('tb_api_log', category, mapper_package, model_package)

    category = 'user'
    if all_targets or category in category_targets:
        mapper_package = mapper_base_pkg + "." + category
        model_package  = model_base_pkg + "." + category

        generate_mybatis('tb_user', category, mapper_package, model_package, sequence = 'tb_user_user_id_seq')

    category = 'zone'
    if all_targets or category in category_targets:
        mapper_package = mapper_base_pkg + "." + category
        model_package  = model_base_pkg + "." + category

        generate_mybatis('tb_swing_zone', category, mapper_package, model_package, sequence = 'tb_swing_zone_zone_id_seq')
        generate_mybatis('tb_book_hist', category, mapper_package, model_package, sequence = 'tb_book_hist_book_id_seq')
        generate_mybatis('tb_usage_hist', category, mapper_package, model_package)

generate_mybatis_files()
