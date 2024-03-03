CREATE DATABASE entreprise;
USE entreprise;

drop table if exists PERSONNE;

create table PERSONNE ( 
  ID       		bigint (6) not null AUTO_INCREMENT,
  NOM     	  varchar (100), 
  PRENOM      varchar (100), 
  AGE        	int ,
  primary key (ID)
);

insert into PERSONNE(NOM,PRENOM,AGE) values ('patrice','radin','20');
insert into PERSONNE(NOM,PRENOM,AGE) values ('natache','tito','20');
insert into PERSONNE(NOM,PRENOM,AGE) values ('sabndrine','quest','20');
insert into PERSONNE(NOM,PRENOM,AGE) values ('lase','tuto','20');

create table SERVICE ( 
  IDSERVICE    	bigint (6) not null AUTO_INCREMENT,
  LABEL     	  varchar (100), 
  primary key (IDSERVICE)
);

insert into SERVICE(LABEL) values ('achat');
insert into SERVICE(LABEL) values ('comptabilite');

create table SERVICEPERSONNE (ID   bigint (6) not null AUTO_INCREMENT, 
  IDSERVICE  bigint (6), 
  IDPERSONNE  bigint (6),
  primary key (ID));

insert into SERVICEPERSONNE(IDSERVICE,IDPERSONNE) values (1,1);
insert into SERVICEPERSONNE(IDSERVICE,IDPERSONNE) values (1,2);
