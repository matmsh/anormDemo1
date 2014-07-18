
drop table if exists  motorcycles;
drop sequence if exists motorcycles_seq;


CREATE TABLE motorcycles ( 
    id  int4 not null,
    make varchar,
    model varchar,
    enginecapacity int,
    primary key (id));
    
 create sequence motorcycles_seq;