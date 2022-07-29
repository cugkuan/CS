package com.k.plugin;

public class CsServiceClassInfo {


    public String className;

    public String  url;

    /**
     * url
     */
    public String  urlKey;

    public CsServiceClassInfo(String className,String url){
        this.className = className;
        this.url = url;
        // url key的生成规则必须和库里面的保持一致性
    }
}
