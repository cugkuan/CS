package com.brightk.cs.core.annotation


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Interceptor(
    val name: String,
    /**
     * 优先级
     * @return
     */
    val priority: Int = 0
)
