# crawler4j-postgres-sample
This is a very simple example how crawler4j can be used to store crawled text material in a PostgreSQL database.

Technology involved:

* [crawler4j](https://github.com/yasserg/crawler4j)
* [PostgreSQL](https://www.postgresql.org/)
* [c3p0](http://www.mchange.com/projects/c3p0/) - Connection Pool

## Setup

- Create a database in PostgreSQL.
- Schema:

```
CREATE SEQUENCE id_master_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE TABLE webpage(id bigint NOT NULL, html TEXT, text TEXT, url varchar(4096), seen timestamp without time zone NOT NULL, primary key (id));
```