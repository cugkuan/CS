package com.brightk.cs.common.exception;

import com.brightk.cs.core.CsException;

public class TimeOutException extends CsException {

    public TimeOutException(){
        super("超时");
    }
    public TimeOutException(String errorMsg){
        super(errorMsg);
    }
}
