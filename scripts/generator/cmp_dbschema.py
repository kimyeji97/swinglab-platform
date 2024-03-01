#!/usr/bin/python
# -*- coding: utf-8 -*-

"""
DB 스키마 비교용 스크립트

@author : Gwanggeun Yoo
"""

import sys , os, io ,re
import datetime

__IS_VERSION_3__= sys.version_info.major == 3

dir_path = os.path.dirname(os.path.realpath(__file__))
sys.path.append(dir_path)

from gen_config import cmp_cons, con_schema

con1 = cmp_cons[0]
con2 = cmp_cons[1]

class Table:
    def __init__(self, tname, fields):
        self.table_name = tname
        self.fields = fields
    def to_str(self):
        return self.table_name + " > " + ','.join(list(map(lambda i:i.name, self.fields))) if self.fields else ''
    def print_info(self):
        print (self.to_str())

class TableField:
    def __init__(self, **kwargs):
        self.name              = kwargs['field'].upper()
        self.is_pk             = kwargs['key'] == 'PRI'
        self.nullable          = kwargs['null'] != 'NO'
        self.type              = kwargs['type']
    def to_str(self):
        return "{:20}\t{}\t{}\t{}".format(self.name, self.is_pk, self.nullable, self.type)
    def print_info(self):
        print (self.to_str)


import psycopg2

#import mysql.connector

# def get_tables(connection_opts):
#     cnx = mysql.connector.connect(**connection_opts)
#     cnx2 = mysql.connector.connect(**connection_opts)
#
#     cursor = cnx.cursor()
#     cursor.execute('show tables',False)
#
#     tables = []
#
#     for row in cursor:
#         tn = row[0]
#         db_fields = get_field_info(tn, cnx2)
#         table = Table(tn, db_fields)
#         tables.append(table)
#         # table.print_info()
#     cursor.close()
#     cnx.close()
#     cnx2.close()
#     return tables

# def get_field_info(table_name , cnx):
#     # cnx = mysql.connector.connect(**connection_opts)
#     cursor = cnx.cursor()
#     cursor.execute('desc ' + table_name,False)
#
#     def to_lower(s):
#         return s.lower()
#     col_names = map(to_lower,cursor.column_names)
#     if __IS_VERSION_3__:
#         col_names = list(col_names)
#
#     rows = []
#     for row in cursor:
#         str_row = None
#         if __IS_VERSION_3__:
#             str_row = list(map(str, row))
#         else:
#             str_row = row
#         map_row = dict(zip(col_names, str_row))
#         field = TableField(**map_row)
#         rows.append(field)
#     cursor.close()
#     # cnx.close()
#     return rows

def get_tables(connection_opts):

    cnx = psycopg2.connect(**connection_opts)
    cnx2 = psycopg2.connect(**connection_opts)

    cursor = cnx.cursor()
    cursor.execute("select table_name from information_schema.tables where table_schema='"+con_schema+"'",False)

    tables = []

    for row in cursor:
        tn = row[0]
        db_fields = get_field_info(tn, cnx2)
        table = Table(tn, db_fields)
        tables.append(table)
        #table.print_info()
    cursor.close()
    cnx.close()
    cnx2.close()
    return tables

def get_field_info(table_name , cnx):
    cursor = cnx.cursor()
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
WHERE 
   table_schema = %(con_schema)s and table_name = %(table_name)s
"""

    cursor.execute(sql,{'table_name':table_name,'con_schema':con_schema})

    def to_lower(s):
        return s.lower()

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
        field = TableField(**map_row)
        rows.append(field)
    cursor.close()
    # cnx.close()
    return rows

old_database=con1['database']
new_database=con2['database']
old_tables = get_tables(con1)
new_tables = get_tables(con2)

table_names1 = list(map(lambda t:t.table_name, old_tables))
table_names2 = list(map(lambda t:t.table_name, new_tables))

def find_table(tn , tables):
    for t in tables:
        if t.table_name == tn:
            return t
    return None
def find_field(sf, fields):
    for f in fields:
        if f.name == sf.name:
            return f
    return None

l1 = 40
l2 = 50
l3 = 40
l4 = 50

format = "{:" + str(l1) + "} | {:" +str(l2)+ "} | {:" + str(l3)+ "} | {:" + str(l4) + "}"

print(format.format("OLD : "+ old_database,"", "NEW : "+ new_database,""))
print(format.format("-"*l1, "-"*l2, "-"*l3, "-"*l4))
print(format.format("OLD TN", "{:20} {}".format("OLD COL","(is_pk, nullable, type)"), "NEW TN", "{:20} {}".format("NEW COL","(is_pk , nullable, type)")))
print(format.format("-"*l1, "-"*l2, "-"*l3, "-"*l4))

data = ""
data += (format.format("OLD : "+ old_database,"", "NEW : "+ new_database,"")) + "\n"
data += (format.format("-"*l1, "-"*l2, "-"*l3, "-"*l4)) + "\n"
data += (format.format("OLD TN", "OLD COL (is_pk, nullable, type)", "NEW TN", "NEW COL (is_pk , nullable, type)")) + "\n"
data += (format.format("-"*l1, "-"*l2, "-"*l3, "-"*l4)) + "\n"

notexist_old = []
notexist_new = []
different = []

temp = []

for t1 in old_tables:
    tn = t1.table_name
    t2 = find_table(t1.table_name , new_tables)
    if t2:
        for f1 in t1.fields:
            f2 = find_field(f1 , t2.fields)
            if f2 == None:
                notexist_new.append(format.format(tn , "{:20} ({}, {}, {})".format(f1.name, f1.is_pk, f1.nullable, f1.type), tn,""))
            elif f1.is_pk != f2.is_pk or f1.nullable != f2.nullable or f1.type != f2.type:
                different.append(format.format(tn , "{:20} ({}, {}, {})".format(f1.name, f1.is_pk, f1.nullable, f1.type), tn,"{:20} ({}, {}, {})".format(f2.name, f2.is_pk, f2.nullable, f2.type)))
    else:
        notexist_new.append(format.format(tn , "", "",""))

for t1 in new_tables:
    tn = t1.table_name
    t2 = find_table(t1.table_name , old_tables)
    if t2:
        for f1 in t1.fields:
            f2 = find_field(f1 , t2.fields)
            if f2 == None:
                notexist_new.append(format.format(tn , "", tn,"{:20} ({}, {}, {})".format(f1.name, f1.is_pk, f1.nullable, f1.type)))
            # elif f1.is_pk != f2.is_pk or f1.nullable != f2.nullable or f1.type != f2.type:
            #     different.append(format.format(tn , "{:20} ({}, {}, {})".format(f2.name, f2.is_pk, f2.nullable, f2.type), tn,"{:20} ({}, {}, {})".format(f1.name, f1.is_pk, f1.nullable, f1.type)))
    else:
        notexist_old.append(format.format("" , "", tn,""))


# print
for no in notexist_old:
    print(no)
    data += no + "\n"

if len(notexist_old) > 0:
    print(format.format("-"*l1, "-"*l2, "-"*l3, "-"*l4))
    data += format.format("-"*l1, "-"*l2, "-"*l3, "-"*l4) + "\n"
for nn in notexist_new:
    print(nn)
    data += nn + "\n"

if len(notexist_new) > 0:
    print(format.format("-"*l1, "-"*l2, "-"*l3, "-"*l4))
    data += format.format("-"*l1, "-"*l2, "-"*l3, "-"*l4) + "\n"
for d in different:
    print(d)
    data += d + "\n"

print(format.format("-"*l1, "-"*l2, "-"*l3, "-"*l4))
data += format.format("-"*l1, "-"*l2, "-"*l3, "-"*l4) + "\n"


# write file
_IS_WINDOW_ = os.name == 'Windows' or os.name == 'nt'
_TEMP_DIR_ = 'C:\\Temp\generator' if _IS_WINDOW_ else os.path.join(os.path.expanduser("~"),"Temp","generator")
DATE_FORMAT="%Y%m%d%H%M%S"
tmpfolder = datetime.datetime.strftime(datetime.datetime.now(),DATE_FORMAT)

tempdir = os.path.join(_TEMP_DIR_, 'cmp_dbschema')
file_nm = 'cmp_dbschema-'+tmpfolder+'.txt'

if not os.path.exists(tempdir):
    print ('tempdir to : {}'.format(tempdir))
    os.makedirs(tempdir)

def write_file_core(path,  file_name, data):
    with open(os.path.join(path,file_name), 'w') as f :
        f.write(data)

write_file_core(tempdir, file_nm, data)
print('Success : Write file -> {}'.format(os.path.join(tempdir, file_nm)))
