package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Created by xuery on 2016/8/21.
 */
@Component
//@Aspect    //定义一个切面 @Aspect
public class LogAspect {
    public static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.nowcoder.controller.*.*(..))")
    public void before(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for(Object arg:joinPoint.getArgs()){
            sb.append("arg::"+arg+"\n");
        }
        System.out.println(sb.toString());
        logger.info("before method");
    }

    @After("execution(* com.nowcoder.controller.*.*(..))")
    public void after(){
        logger.info("after method");
    }
}
