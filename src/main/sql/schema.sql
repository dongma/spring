-- 数据库初始化的脚本

-- 创建秒杀系统的数据库seckill
create database if not EXISTS `seckill`;
-- 使用数据库seckill
use seckill;

-- 创建秒杀库存表seckill.
create table if not exists `seckill` (
  seckill_id bigint not null AUTO_INCREMENT comment '商品库存id',
  name varchar(120) not null comment '商品名称',
  number int not null comment '商品库存',
  start_time TIMESTAMP not null comment '秒杀开始的时间',
  end_time TIMESTAMP not null comment '秒杀结束的时间',
  create_time TIMESTAMP not null default current_timestamp comment '秒杀创建的时间',
  -- 选择seckill_id作为秒杀系统表的主键id,primary key
  PRIMARY KEY (seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 default charset=utf8 comment='秒杀库存表';

-- 对于秒杀库存表的一些初始化的数据
insert into seckill(name, number, start_time, end_time)
  values('1000元秒杀iphone6', 100, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
  ('500元秒杀小米6', 50, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
  ('600元秒杀ipad', 120, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
  ('元秒杀小米6', 50, '2015-11-01 00:00:00', '2015-11-02 00:00:00');

-- 秒杀成功明细表,用户登陆认证相关的信息.
create table if not exists 'success_killed'(
  seckill_id bigint not null comment '秒杀商品的id',
  user_phone bigint not null comment '用户手机号',
  state tinyint not null default -1 comment '状态表示：-1表示无效,0表示成功,1,已经付款',
  create_time timestamp not null comment '创建时间',
  primary key(seckill_id, user_phone), -- 创建的是联合主键
  key idx_create_time(create_time)
)ENGINE=InnoDB default charset='utf8' comment = '秒杀成功明细表';
