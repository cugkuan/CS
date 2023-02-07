package com.brightk.cs.core.annotation;

import com.brightk.cs.core.ServiceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CsUri {
    String uri();
    ServiceType type() default ServiceType.DEFAULT;
}
