use tuling;

-- explain
DROP TABLE IF EXISTS `actor`;
CREATE TABLE `actor` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `actor` (`id`, `name`, `update_time`) VALUES (1,'a','2017-12-22 15:27:18'), (2,'b','2017-12-22 15:27:18'), (3,'c','2017-12-22 15:27:18');

DROP TABLE IF EXISTS `film`;
CREATE TABLE `film` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `film` (`id`, `name`) VALUES (3,'film0'),(1,'film1'),(2,'film2');

DROP TABLE IF EXISTS `film_actor`;
CREATE TABLE `film_actor` (
  `id` int(11) NOT NULL,
  `film_id` int(11) NOT NULL,
  `actor_id` int(11) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_film_actor_id` (`film_id`,`actor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `film_actor` (`id`, `film_id`, `actor_id`) VALUES (1,1,1),(2,1,2),(3,2,1);

-- 简单使用
explain select * from actor;

-- explain extended
select * from film where id = 1;
explain extended select * from film where id = 1; -- TODO: 没有通过
show warnings; -- TODO: 没有成功

-- explain字段解析
-- id 字段
-- 几个select 有几个id id越大 执行优先级越高

-- select_type 字段
-- SIMPLE表示简单查询 不包含子查询union
explain select * from film where id = 2;

set session optimizer_switch = 'derived_merge=off'; -- 关闭衍生表的合并优化
-- PRIMARY类型 主查询
-- DERIVED类型 from后面的查询语句
-- SUBQUERY类型 from前面的查询语句
explain select (select 1 from actor where id = 1) from (select * from film where id = 1) der;
set session optimizer_switch = 'derived_merge=on'; -- 还原配置

-- union类型
explain select 1 union all select 1;

-- table 字段
-- 表示explain正在访问那张表
explain select min(id) from film; -- 优化后 不需要访问表 table字段为空

-- partitions 字段

-- type 字段(关联类型或访问类型)
-- system > const > eq_ref > ref > range > index > all

-- system const
explain select * from (select * from film where id = 1) tmp;

-- eq_ref
explain select * from film_actor left join film on film_actor.film_id = film.id;

-- ref
explain select * from film where name = 'film1';
explain select film_id from film left join film_actor on film.id = film_actor.film_id;

-- range
explain select * from actor where id > 1;

-- index 索引使用的是idx_name
explain select * from film;

-- all
explain select * from actor;

-- possible_keys 字段
-- 可能会被使用的索引

-- key列
-- 被使用的索引

-- key_len
-- 索引类型的字节数
explain select * from film_actor where film_id = 2;
-- key_len 计算规则
-- char(n) 数字或字母n个字节 汉字3n个字节
-- varchar(n) 数字或字母(n+2)个字节 汉字(3n+2)个字节 2个字节存字符串长度
-- tinyint 1个字节
-- smallint 2个字节
-- int 4个字节
-- bigint 8个字节
-- date 3个字节
-- timestamp 4个字节
-- datetime 8个字节
-- 字段允许为空 需要额外一个字节存储该信息

-- ref 字段

-- rows 字段
-- sql估计要读取并检测的行数

-- filter 字段

-- extra 字段
explain select film_id from film_actor where film_id = 1; -- 索引排序
explain select * from actor where name = 'a'; -- 使用where 未使用索引
explain select * from film_actor where film_id > 1; -- 使用部分索引
explain select distinct name from actor; -- 临时表 针对distinct
explain select distinct name from film; -- 索引排序
explain select * from actor order by name; -- 文件排序
explain select * from film order by name; -- 索引排序
explain select min(id) from film; -- 使用函数访问索引的某个字段

-- 索引的实践
use tuling;

CREATE TABLE `employees` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(24) NOT NULL DEFAULT '' COMMENT '姓名',
  `age` int(11) NOT NULL DEFAULT '0' COMMENT '年龄',
  `position` varchar(20) NOT NULL DEFAULT '' COMMENT '职位',
  `hire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入职时间',
  PRIMARY KEY (`id`),
  KEY `idx_name_age_position` (`name`,`age`,`position`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='员工记录表';

INSERT INTO employees(name,age,position,hire_time) VALUES('LiLei',22,'manager',NOW());
INSERT INTO employees(name,age,position,hire_time) VALUES('HanMeimei', 23,'dev',NOW());
INSERT INTO employees(name,age,position,hire_time) VALUES('Lucy',23,'dev',NOW());

-- 全值匹配
explain select * from employees where name = 'LiLei'; -- key_len/74  ref/const
explain select * from employees where name = 'LiLei' and age = 22; -- key_len/78 ref/const,const
explain select * from employees where name = 'LiLei' and age = 22 and position = 'manager'; -- key_len/140 ref/const,const,const

-- 最左前缀法则
explain select * from employees where name = 'Bill' and age = 31; -- 使用了索引
explain select * from employees where age = 30 and position = 'dev'; -- 没有使用索引 不满足最左前缀法则
explain select * from employees where position = 'manager'; -- 没有使用索引 不满足最左前缀法则

-- 使用函数 导致索引失效
explain select * from employees where name = 'LiLei'; -- 使用了索引
explain select * from employees where left(name, 3) = 'LiLei'; -- 使用函数表达式 使索引失效

-- 时间戳 使用函数 导致索引失效
alter table `employees` add index `idx_hire_time` (`hire_time`) using btree; -- 创建索引
explain select * from employees where date(hire_time) = '2018-09-30'; -- 使用函数没有使用索引
explain select * from employees where hire_time >= '2018-09-30 00:00:00' and hire_time <= '2018-09-30 23:59:59';
alter table `employees` drop index `idx_hire_time`; -- 删除索引

-- 部分满足最左前缀法则
explain select * from employees where name = 'LiLei' and age = 22 and position = 'manager'; -- key_len/140
explain select * from employees where name = 'LiLei' and age > 22 and position = 'manager'; -- key_len/78 索引使用到age为止

-- 减少 select * 的使用 关注extra字段 TODO: 有什么区别 理解下来都使用了覆盖索引
explain select name, age, position from employees where name = 'LiLei' and age = 23 and position = 'manager';
explain select * from employees where name = 'LiLei' and age = 23 and position = 'manager';

-- != 导致没有使用索引
explain select * from employees where name != 'LiLei';

-- is null, is not null都不会走索引
explain select * from employees where name is not null;

-- 通配符开头 全表扫描
explain select * from employees where name like '%Lei'; -- 没有使用索引
explain select * from employees where name like 'Lei%'; -- 使用了索引

-- 字符串不添加引号 不走索引
explain select * from employees where name = '1000';
explain select * from employees where name = 1000;

-- 少用or或者in 可能不会走索引
explain select * from employees where name = 'LiLei' or name = 'HanMeiMei'; -- 没有使用索引

-- 范围查询 可能走索引 可能不走
alter table `employees` add index `idx_age` (`age`) using btree;
explain select * from employees where age >= 1 and age <= 2000; -- 走了索引
explain select * from employees where age >= 1 and age <= 1000; -- 拆分小范围 能帮助走索引
explain select * from employees where age >= 1000 and age <= 2000;
alter table `employees` drop index `idx_age`;

