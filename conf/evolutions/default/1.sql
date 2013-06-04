
# --- !Ups
set ignorecase true;

CREATE TABLE motorcycles ( 
    id  bigint not null,
    make varchar,
    model varchar,
    enginecapacity int,
    constraint pk_motorcycle primary key (id));
    
create sequence motorcycles_seq start with 1000;
    
# --- !Downs
DROP TABLE IF EXISTS motorcycles; 


