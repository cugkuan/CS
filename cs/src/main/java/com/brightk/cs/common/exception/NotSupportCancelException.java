package com.brightk.cs.common.exception;

import com.brightk.cs.core.CsException;

public class NotSupportCancelException extends CsException {

    public NotSupportCancelException(){
        super("不支持取消操作，可以使用CanCancelUriRequestBuild");
    }
}
