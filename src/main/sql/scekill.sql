-- 执行秒杀的存储过程
DELIMITER $$  -- console ;转换为$$
-- 定义存储过程
-- in代表的是输入的参数 our代表的是输出的参数
-- row_count():返回上一条修改类型sql影响的行数(insert,update,delete)
-- row_count: 0代表的是影响的记录数为0 >0代表的是影响的行数 <0表示的是执行sql语句发生内部的错误.
create procedure `seckill`.`execute_seckill`
  ( in v_seckill_id bigint,     -- 秒杀商品的id编号
    in v_phone bigint,          -- 用户电话的号码
    in v_kill_time timestamp,   -- 秒杀的时间点
    out r_result int            -- 秒杀是否成功
  )
  begin
    -- 声明insert_count代表的是执行插入操作影响的行数.
    declare insert_count int default 0;
    start TRANSACTION;          -- 开启事务.
    insert into success_killed
      (seckill_id, user_phone, create_time)
      values(v_seckill_id, v_phone, v_kill_time);
    -- row_count()返回执行上一条DML语句影响的行数.
    select row_count() into insert_count;
    if (insert_count = 0) then
      rollback;
      set r_result = -1;      -- 没有进行插入记录成功
    elseif (insert_count < 0) then
      rollback;
      set r_result = -2;      -- 发生了内部语法错误
    else
      update seckill          -- 更新秒杀商品表的信息,将商品的数量进行递减.
        set number = number - 1
        where seckill_id = v_seckill_id
          and end_time > v_kill_time
          and start_time < v_kill_time
          and number > 0;
      -- 调用row_count()查询记录影响的行数
      select row_count() into insert_count;
      if (insert_count = 0) then
        rollback;
        set r_result = 0;
      elseif (insert_count < 0) then
        rollback;
        set r_result = -2;
      else
        commit;               -- 对事物进行提交.
        set r_result = 1;
      end if;
    end if;
  end;
$$  -- 存储过程定义结束

DELIMITER ;
set @r_result = -3;
-- 执行存储过程
call execute_seckill(1003, 13502178891, now(), @r_result);


