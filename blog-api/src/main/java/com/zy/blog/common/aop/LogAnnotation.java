package com.zy.blog.common.aop;

import java.lang.annotation.*;

/**
 * @author 张岩
 * @version 1.0
 */
//ElementType.TYPE代表可以放在类上面  method代表可以放在方法上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operation() default "";
}
