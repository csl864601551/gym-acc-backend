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
 
```sql t_code_attr转移物流节点数据至t_code
ALTER TABLE `t_code` ADD `storage_record_id` bigint(255) NULL DEFAULT NULL COMMENT '最新流转id' AFTER `company_id`;
ALTER TABLE `t_code` ADD `storage_type` int(255) NULL DEFAULT NULL COMMENT '最新流转类型' AFTER `company_id`;
ALTER TABLE `t_code` ADD `tenant_id` bigint(20) NULL DEFAULT NULL COMMENT '经销商ID' AFTER `company_id`;

```
#### Beta V1.0.7 2021/07/12
 
```sql 添加single生码记录  t_code_attr表添加single_id
CREATE TABLE `t_code_single` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `company_id` bigint(20) DEFAULT NULL COMMENT '企业ID',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `type` int(11) DEFAULT NULL COMMENT '生码类型',
  `count` int(11) DEFAULT NULL COMMENT '码数量',
  `index_start` bigint(20) DEFAULT NULL COMMENT '起始流水号',
  `index_end` bigint(20) DEFAULT NULL COMMENT '终止流水号',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8 COMMENT='生码记录表';
ALTER TABLE `t_code` ADD `single_id` bigint(20) NULL DEFAULT NULL COMMENT '生码记录ID' AFTER `code_attr_id`;
ALTER TABLE `t_code_attr` ADD `single_id` bigint(20) NULL DEFAULT NULL COMMENT '生码记录ID' AFTER `record_id`;
```


#### Beta V1.0.8 2021/07/20
CREATE TABLE `t_security_code_template`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模板名称',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '防伪模板内容',
  `type` int(2) NULL DEFAULT NULL COMMENT '类型',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '企业id',
  `scenario` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '显示场景',
  `is_open` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否启用',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;
ALTER TABLE `t_product` ADD `template_id1` bigint(20) NULL DEFAULT NULL COMMENT '防伪模板1' ;
ALTER TABLE `t_product` ADD `template_id2` bigint(20) NULL DEFAULT NULL COMMENT '防伪模板2' ;
ALTER TABLE `t_product` ADD `template_content1` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '防伪模板1内容' ;
ALTER TABLE `t_product` ADD `template_content2` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '防伪模板2内容' ;
ALTER TABLE `t_product` ADD `content1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单次查询内容' ;
ALTER TABLE `t_product` ADD `content2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '多次查询内容' ;
ALTER TABLE `t_product` alter column product_detail_mobile varchar(255)

#### Beta V1.0.5 2021/07/23
CREATE TABLE `t_quota` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
`param_name` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '参数名称',
`param_key` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '参数键名',
`param_value` decimal(32,2) DEFAULT NULL COMMENT '参数值',
`company_id` bigint(20) DEFAULT NULL COMMENT '企业ID',
`remark` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
`create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='配额表 ';
CREATE TABLE `t_purchase_record` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
`type` int(11) DEFAULT '0' COMMENT '购买类型 默认0-购码',
`order_no` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL,
`count` int(11) NOT NULL COMMENT '购买数量',
`company_id` bigint(20) DEFAULT NULL COMMENT '企业ID',
`purchase_amount` decimal(32,2) DEFAULT NULL COMMENT '金额',
`purchase_type` int(11) DEFAULT NULL COMMENT '类型 0-消费，1-退款',
`remark` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
`create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='消费记录表 ';
CREATE TABLE `t_price_config` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
`name` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
`price` decimal(32,2) DEFAULT NULL COMMENT '价格',
`count` bigint(20) DEFAULT NULL COMMENT '数量 码量',
`type` bigint(20) DEFAULT NULL COMMENT '类型 1-码',
`is_open` varchar(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '是否启用 0-关闭1-开启',
`remark` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
`create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='价格表 平台设置码包价格';
CREATE TABLE `t_payment_record` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`order_no` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单号',
`pay_amount` decimal(32,2) DEFAULT NULL COMMENT '支付金额',
`currency` int(11) DEFAULT '0' COMMENT '币种 默认0-人民币',
`pay_status` int(11) DEFAULT NULL COMMENT '支付状态 0:待支付、1:已支付、2:取消',
`pay_type` int(11) DEFAULT '0' COMMENT '支付方式 0:线下、1:支付宝、2:微信、3:银联、4:其他',
`company_id` bigint(20) DEFAULT NULL COMMENT '企业ID',
`remark` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
`create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='充值记录表 ';

