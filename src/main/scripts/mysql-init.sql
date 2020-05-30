drop database if exists beerinventoryservice;
drop user if exists 'beer_inventory_service'@'%';
create database if not exists beerinventoryservice character set utf8mb4 collate utf8mb4_unicode_ci;
create user if not exists 'beer_inventory_service'@'%' identified with mysql_native_password by 'password';
grant select,insert,update,delete,create,drop,references,index,alter,execute,create view,show view,create routine,
 alter routine,event,trigger on beerinventoryservice.* to 'beer_inventory_service'@'%';
flush privileges;