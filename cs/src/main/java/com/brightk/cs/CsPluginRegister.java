package com.brightk.cs;

import com.brightk.cs.core.ComponentServiceManger;

/**
 * 给插件使用的
 */
public class CsPluginRegister {
    /**
     * 注册服务,给
     * @param key
     * @param className
     */
    public static void register(String key, String className) {
        ComponentServiceManger.pluginRegister(key, className);
    }
}
