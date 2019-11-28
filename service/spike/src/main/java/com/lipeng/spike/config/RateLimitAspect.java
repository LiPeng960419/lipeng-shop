package com.lipeng.spike.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lipeng 910138
 * @Date: 2019/10/14 16:13
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimitAspect {

    double value() default 5.0; //每秒并发数

    long timeOut() default 0; //毫秒

}