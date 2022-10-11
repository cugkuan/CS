package com.brightk.cs.common;

public class NotSupportCancelException extends Exception{

    public NotSupportCancelException(){
        super("不支持取消操作，可以使用CanCancelUriRequestBuild");
    }
}
