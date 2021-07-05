#### Beta V1.0.1 2021/06/18
 
```sql 添加生码记录生成状态和成功记录
ALTER TABLE `t_code_record` MODIFY COLUMN `tray_count` int(11) NULL DEFAULT NULL COMMENT '托数量' AFTER `type`;
```

#### Beta V1.0.6 2021/07/05
 
```sql 添加生码记录生成状态和成功记录
ALTER TABLE `t_code` ADD `storage_record_id` bigint(255) NULL DEFAULT NULL COMMENT '最新流转id' AFTER `company_id`;
ALTER TABLE `t_code` ADD `storage_type` int(255) NULL DEFAULT NULL COMMENT '最新流转类型' AFTER `company_id`;
ALTER TABLE `t_code` ADD `tenant_id` bigint(20) NULL DEFAULT NULL COMMENT '经销商ID' AFTER `company_id`;
```