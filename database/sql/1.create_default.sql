drop schema public cascade;
create schema public;
grant all on schema public to swinglab;

-- 테이블 생성정 미리 되어 있어야함.
CREATE DOMAIN SID AS BIGINT;

-- sid 도메인을 가진 PK의 시퀀스 생성 함수.
CREATE OR REPLACE FUNCTION public.create_seq()
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
	declare 
		sl record;
		s varchar;
		c varchar;
		t varchar;
	BEGIN
		for sl in select * from (
				SELECT c.table_name , c.column_name 
					FROM INFORMATION_SCHEMA.columns c
				inner join information_schema.table_constraints tc on tc.table_schema = c.table_schema 
					and tc.table_catalog = c.table_catalog 
					and tc.table_name  = c.table_name and tc.constraint_type = 'PRIMARY KEY'
				inner join information_schema.key_column_usage kcu on kcu.constraint_name = tc.constraint_name
				     and kcu.constraint_schema = tc.constraint_schema
				     and kcu.table_name = tc.table_name 
				     and kcu.table_schema = tc.table_schema 
				     and kcu.table_catalog  = tc.table_catalog 
				     and kcu.column_name  = c.column_name 
				where c.table_schema =current_schema and c.table_catalog = current_database() and c.data_type = 'bigint' and c.domain_name = 'sid'
				except
				SELECT c.table_name , c.column_name 
				FROM INFORMATION_SCHEMA.columns c
				inner join information_schema.table_constraints tc on tc.table_schema = c.table_schema 
					and tc.table_catalog = c.table_catalog 
					and tc.table_name  = c.table_name and tc.constraint_type = 'FOREIGN KEY'
				inner join information_schema.key_column_usage kcu on kcu.constraint_name = tc.constraint_name
				     and kcu.constraint_schema = tc.constraint_schema
				     and kcu.table_name = tc.table_name 
				     and kcu.table_schema = tc.table_schema 
				     and kcu.table_catalog  = tc.table_catalog 
				     and kcu.column_name  = c.column_name 
				where c.table_schema =current_schema and c.table_catalog = current_database() and c.data_type = 'bigint' and c.domain_name = 'sid'
			) a order by a.table_name
		loop
			s := concat(sl.table_name,'_',sl.column_name,'_seq');
			c := sl.column_name;
			t := sl.table_name;
			raise notice '% > %',sl,s;
			execute format('create sequence if not exists %s',s);
			execute format('ALTER TABLE %s alter column %s SET DEFAULT nextval(''%s'')',t,c,s);
			execute format('alter sequence %s owned by %s.%s',s,t, c);
		end loop;
		return 0;
	END;
$function$
;


CREATE OR REPLACE FUNCTION public.create_partition(table_name text, partition_name text ,start_dt text,end_dt text)
RETURNS integer 
LANGUAGE plpgsql
AS $procedure$
BEGIN
	execute format('CREATE TABLE IF NOT EXISTS %s PARTITION OF %s FOR VALUES FROM (''%s'') TO (''%s'')', partition_name, table_name, start_dt, end_dt);
	return 0;
END;
$procedure$
;


CREATE OR REPLACE FUNCTION public.drop_table(table_name text)
RETURNS integer 
LANGUAGE plpgsql
AS $procedure$
BEGIN
	execute format('DROP TABLE IF EXISTS %s', table_name);
	return 0;
END;
$procedure$
;

