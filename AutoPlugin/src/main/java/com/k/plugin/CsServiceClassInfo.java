package com.k.plugin;

public class CsServiceClassInfo {
    public final String className;
    public final String  url;
    public final String  urlKey;
    public CsServiceClassInfo(String className,String url) {
        this.className = className.replace('/','.');
        this.url = url;
        // url key的生成规则必须和库里面的保持一致性
        urlKey = CsUtils.getKey(url);
    }
    @Override
    public String toString() {
        return "className:"+className +" url:"+url + " urlKey:"+urlKey;
    }
}
