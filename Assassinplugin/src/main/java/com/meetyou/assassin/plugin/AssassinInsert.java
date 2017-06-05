package com.meetyou.assassin.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Linhh on 17/6/5.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AssassinInsert {
    String[] value() default {""};
}