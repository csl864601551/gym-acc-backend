#### Beta V1.0.1 2021/06/18
 
```sql 添加生码记录生成状态和成功记录
ALTER TABLE `t_code_record` MODIFY COLUMN `tray_count` int(11) NULL DEFAULT NULL COMMENT '托数量' AFTER `type`;
```
