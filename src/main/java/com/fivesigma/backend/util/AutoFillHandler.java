package com.fivesigma.backend.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author Andy
 * @date 2022/10/14
 */

@Component
public class AutoFillHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("user_id", RandomStringUtils.randomNumeric(10), metaObject);
        this.setFieldValByName("task_id", RandomStringUtils.randomAlphabetic(10), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
