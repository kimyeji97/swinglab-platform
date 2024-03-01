#!/usr/bin/python3
# -*- coding: utf-8 -*-

fr1 = open("1.create_default.sql", 'r', encoding='utf-8')
fr2 = open("2.create_query.sql", 'r', encoding='utf-8')
fr3 = open("3.create_finally.sql", 'r', encoding='utf-8')
fr4 = open("4.insert.sql", 'r', encoding='utf-8')

fw = open("create.sql", 'w', encoding='utf-8')

lines = fr1.readlines()
for line in lines:
	fw.write(line)

lines = fr2.readlines()
for line in lines:
#    line = line.replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS")
#    line = line.replace("INSERT INTO _cub_schema_comments (table_name, column_name, description, last_updated, last_updated_user) VALUES ('", "comment on column ")
#    line = line.replace("', '", ".", 1)
#    line = line.replace("', '", " is '", 1)
#    line = line.replace("', systimestamp, CURRENT_USER);", "';");
    fw.write(line)

lines = fr3.readlines()
for line in lines:
	fw.write(line)
	
lines = fr4.readlines()
for line in lines:
	fw.write(line)

fr1.close()
fr2.close()
fr3.close()
fr4.close()
fw.close()