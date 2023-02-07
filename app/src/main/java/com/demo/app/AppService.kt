package com.demo.app

import com.brightk.cs.core.CsService
import com.brightk.cs.core.OnRequestResultListener
import com.brightk.cs.core.UriRequest
import com.brightk.cs.core.annotation.CsUri

@CsUri(uri = "app://app/service")
class AppService :CsService{
    override fun call(uriRequest: UriRequest, listener: OnRequestResultListener?) {
    }
}