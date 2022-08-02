package com.brightk.cs.common;

public class CsNotSupportParamsException extends Exception{

    public CsNotSupportParamsException(Object value){
        super("不支持该类型--"+value.getClass()+"参数传递");
    }
}
