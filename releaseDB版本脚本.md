#### Beta V1.0.1 2021/06/18
 
```sql 添加生码记录生成状态和成功记录
ALTER TABLE `t_code_record` MODIFY COLUMN `tray_count` int(11) NULL DEFAULT NULL COMMENT '托数量' AFTER `type`;
```
#### Beta V1.0.5 2021/07/01
 
```sql 添加打印表
CREATE TABLE `t_print_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_id` bigint(11) DEFAULT NULL COMMENT '企业Id',
  `box_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '箱码',
  `code_index` bigint(20) DEFAULT NULL COMMENT '流水号',
  `print_status` int(1) unsigned zerofill DEFAULT '0' COMMENT '打印状态（0：未打印，1：已打印）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='打印数据';
```

#### Beta V1.0.6 2021/07/05
 
```sql 添加生码记录生成状态和成功记录
ALTER TABLE `t_code` ADD `storage_record_id` bigint(255) NULL DEFAULT NULL COMMENT '最新流转id' AFTER `company_id`;
ALTER TABLE `t_code` ADD `storage_type` int(255) NULL DEFAULT NULL COMMENT '最新流转类型' AFTER `company_id`;
ALTER TABLE `t_code` ADD `tenant_id` bigint(20) NULL DEFAULT NULL COMMENT '经销商ID' AFTER `company_id`;
```