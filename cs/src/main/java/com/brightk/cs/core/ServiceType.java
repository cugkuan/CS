package com.brightk.cs.core;

/**
 * 服务的创建方式
 * single ---> 单例模式
 * new --> 每次使用都创建一个新的
 */
public enum ServiceType {
    DEFAULT,
    SINGLE,
    NEW
}
