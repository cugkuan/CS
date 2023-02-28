package com.k.plugin;

import java.util.ArrayList;
import java.util.List;

public class CsPluginUtils {
    /**
     * 服务地址的配置注解
     */
    public static String SERVICE_CS_URI = "Lcom/brightk/cs/core/annotation/CsUri;";
    /**
     * 服务注解配置的uri
     */
    public static String SERVICE_CS_URI_FILE_NAME = "uri";
    public static String SERVICE_CS_URI_FILE_TYPE = "type";
    /**
     * 继承的服务
     */
    public static String SERVICE_CS_CLASS = "com/brightk/cs/core/CsService";
    /**
     * 自动注入的目标
     */
    public static final String AUTO_REGISTER_TARGET = "Lcom/brightk/cs/core/annotation/AutoRegisterTarget;";
    /**
     *  继承服务是发现的对象
     */
    public static String FIND_SERVICE_CLASS_TARGET = "com/brightk/cs/core/CsService";
    /**
     *
     */
    public static final String AUTO_REGISTER_CLASS = "com.brightk.cs.CS";
    public static List<CsServiceClassInfo> csServiceClassInfoList = new ArrayList<>();
    static boolean isFinishInject = false;

    public static String[] scanPackage;
    public static void clear() {
        csServiceClassInfoList.clear();
        isFinishInject = false;
    }

}
