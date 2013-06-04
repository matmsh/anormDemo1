
# --- !Ups

CREATE TABLE MOTORCYCLES ( 
    make varchar,
    model varchar,
    enginecapacity int);
    
# --- !Downs
DROP TABLE IF EXISTS MOTORCYCLES; 


