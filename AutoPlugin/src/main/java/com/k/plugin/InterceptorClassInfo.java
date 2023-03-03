package com.k.plugin;

public class InterceptorClassInfo {
    public String className;
    public int   priority;
    public final String  name;
    public InterceptorClassInfo(String className, int priority, String name) {
        this.className = className;
        this.priority = priority;
        this.name = name;
    }
    @Override
    public String toString() {
        return "className:"+className +" priority:"+priority + " name:"+name;
    }
}
