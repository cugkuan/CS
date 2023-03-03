package com.brightk.cs.common.exception;


import com.brightk.cs.core.CsException;

public class UriNullException extends CsException {
    public UriNullException(){
        super("Url不能为null");
    }

}
