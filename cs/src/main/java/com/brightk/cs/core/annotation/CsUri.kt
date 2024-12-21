package com.brightk.cs.core.annotation

import com.brightk.cs.core.ServiceType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CsUri(val uri: String, val type: ServiceType = ServiceType.DEFAULT)
