package com.helper.demo.conf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int tokens() default 10;    // Number of tokens
    int minutes() default 1;    // Time period in minutes
    int maxWaitMs() default 1000; // Maximum wait time in milliseconds
}
