-- 秒杀操作存储过程
-- 将console的；转换成$$,因为sql语句中的结束字符也是；所以将存储过程的结束字符改成$$以便区分
DELIMITER $$
-- 定义存储过程，`seckill`表示对seckill表有影响，.后面的表示该存储过程名称
-- in:输入参数  out:输出参数
-- row_count()：返回上一条修改类型sql影响行数（0：未修改；>0：修改行数；<0：sql错误）
CREATE PROCEDURE `seckill`.`execute_seckill`
  (IN v_seckill_id BIGINT, IN v_phone VARCHAR(20), IN v_kill_time TIMESTAMP, OUT r_result INT)
  BEGIN
    -- 定义一个变量（在存储过程中定义，和在console中定义不同）
    DECLARE insert_count INT DEFAULT -1;
    START TRANSACTION;
    INSERT IGNORE INTO success_killed(seckill_id,user_phone,create_time,state) VALUES (v_seckill_id,v_phone,v_kill_time,0);
    SELECT row_count() INTO insert_count;
    IF(insert_count = 0) THEN
      ROLLBACK ;
      SET r_result = -1;
    ELSEIF (insert_count < 0) THEN
      ROLLBACK ;
      SET r_result = -2;
    ELSE
      UPDATE seckill SET number = number - 1
      WHERE seckill_id = v_seckill_id AND start_time < v_kill_time AND end_time > v_kill_time AND number > 0;
      SELECT row_count() INTO  insert_count;
      IF (insert_count = 0) THEN
        ROLLBACK ;
        SET r_result = 0;
      ELSEIF(insert_count < 0) THEN
        ROLLBACK ;
        SET r_result = -2;
      ELSE
        COMMIT ;
        SET r_result = 1;
      END IF ;
    END IF;
  END $$
-- 存储过程定义结束，将换行符换回;,开始调用存储过程
DELIMITER ;
-- 定义一个变量（这是在console中定义，和在存储过程中定义不同）
SET @r_result = -3;
-- 执行存储过程
CALL execute_seckill(1003, 12345678923, now(), @r_result);
-- 获取结果
SELECT @r_result;

-- 存储过程可优化事务行级锁持有时间，但不可过度依赖