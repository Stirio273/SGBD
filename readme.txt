Fonctionnalites :
	_creation database, table
	_afficher liste base de donnees, liste tables
	_insertion, mise à jour, suppression
	_jointure, triage ordre croissant
	_selection (avec condition)

Syntax :
	_create database <name>;
	_create table <name>;
	_show tables;
	_show databases;
	_insert into <tableName> values(value1,value2,...);
	_update <tableName> set colonne1=value1,colonne2=value2,... where ... or .. and ...
	_delete from <tableName> where (condition);
	_select * from <table1> join <table2> on <table1>.colonne=<table2>.colonne;
	_select * from <tableName> order by colonne;
	_select * from <tableName> where (condition); 
