use tuling;

-- 插入一些示例数据
drop procedure if exists insert_emp;
delimiter ;
create procedure insert_emp()
begin
    declare i int;
    set i = 1;
    while(i < 100000) do
        insert into employees(name, age, position) values(CONCAT('zhuge',i),i,'dev');
        set i = i + 1;
    end while;
end;;
delimiter ;
call insert_emp();

-- 联合索引第一个字段使用范围查询 不走索引
explain select * from employees where name > 'LiLei' and age = 22 and position = 'manager';
explain select * from employees force index(idx_name_age_position) where name > 'LiLei' and age = 22 and position = 'manager'; -- 强制走索引
set global query_cache_size = 0;
set global query_cache_type = 0;
select * from employees where name > 'LiLei';
select * from employees force index(idx_name_age_position) where name > 'LiLei'; -- 执行时间反而更长

-- 覆盖索引优化 查询字段本身就是索引字段
explain select name, age, position from employees where name > 'LiLei' and age = 22 and position = 'manager';

-- or in 在数据量大的时候会走索引
explain select * from employees where name in ('LiLei', 'HanMeimei', 'Lucy') and age = 22 and position = 'manager';
explain select * from employees where (name = 'LiLei' or name = 'HanMeimei') and age = 22 and position = 'manager';

-- 创建一张副本表 再使用 in or 关键字查询
use tuling;
CREATE TABLE `employees_copy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(24) NOT NULL DEFAULT '' COMMENT '姓名',
  `age` int(11) NOT NULL DEFAULT '0' COMMENT '年龄',
  `position` varchar(20) NOT NULL DEFAULT '' COMMENT '职位',
  `hire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入职时间',
  PRIMARY KEY (`id`),
  KEY `idx_name_age_position` (`name`,`age`,`position`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='员工记录表';

INSERT INTO employees_copy(name,age,position,hire_time) VALUES('LiLei',22,'manager',NOW());
INSERT INTO employees_copy(name,age,position,hire_time) VALUES('HanMeimei', 23,'dev',NOW());
INSERT INTO employees_copy(name,age,position,hire_time) VALUES('Lucy',23,'dev',NOW());

explain select * from employees_copy where (name = 'LiLei' or name = 'HanMeimei') and age = 22 and position = 'manager'; -- 理论上不应该走索引 但是走了

-- like key% 会走索引
explain select * from employees where name like 'LiLei%' and age = 22 and position = 'manager';
explain select * from employees_copy where name like 'LiLei%' and age = 22 and position = 'manager';

explain select * from employees where name > 'a'; -- 不会使用索引 因为使用索引的代价比全表扫描来的大
explain select name, age, position from employees where name > 'a'; -- 使用了索引数 不用回表
explain select * from employees where name > 'zzz'; -- 走索引的特例 此时需要trace工具来辅助分析

-- 开启trace
set session optimizer_trace="enabled=on",end_markers_in_json=on;

-- 查看trace字段可知索引扫描的成本低于全表扫描，所以mysql最终选择索引扫描
select * from employees where name > 'a' order by position; -- TODO: 没有成功
select TRACE from information_schema.OPTIMIZER_TRACE;
select * from employees where name > 'zzz' order by position;
select * from information_schema.OPTIMIZER_TRACE;

-- 关闭trace
set session optimizer_trace="enabled=off";

-- order by 优化
explain select * from employees where name = 'LiLei' and position = 'dev' order by age; -- 没有跳过索引字段 使用索引排序
explain select * from employees where name = 'LiLei' order by position; -- 跳过索引字段 使用文件排序
explain select * from employees where name = 'LiLei' order by age, position; -- TODO: 为什么既不是文件排序 也不是索引排序
explain select * from employees where name = 'LiLei' order by position, age; -- position, age颠倒 没有走索引排序
explain select * from employees where name = 'LiLei' and age = 18 order by position, age; -- 虽然position, age颠倒 但是age是常量
explain select * from employees where name = 'zhuge' order by age asc, position desc; -- position需要降序 和索引树不一致 需要使用文件排序
explain select * from employees where name in ('LiLei', 'zhuge') order by age, position; -- 使用了索引 多个相等的条件也是范围查询
explain select * from employees where name > 'a' order by name; -- 没有走索引 文件排序
explain select name, age, position from employees where name > 'a' order by name; -- 走索引 索引排序

-- TODO: 慢查询优化
-- http://note.youdao.com/noteshare?id=c71f1e66b7f91dab989a9d3a7c8ceb8e&sub=0B91DF863FB846AA9A1CDDF431402C7B




