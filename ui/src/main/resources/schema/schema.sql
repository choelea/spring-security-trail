DROP DATABASE   if exists `ui`;
CREATE SCHEMA `ui` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
grant all privileges on ui.* to 'okchem'@'%' identified by 'okchem';