package com.woniu.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AutoFill implements MetaObjectHandler {

    @Override
    public void insertFill(org.apache.ibatis.reflection.MetaObject metaObject) {
       this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModifified",new Date(),metaObject);
    }

    @Override
    public void updateFill(org.apache.ibatis.reflection.MetaObject metaObject) {
        this.setFieldValByName("gmtModifified",new Date(),metaObject);
    }
}
