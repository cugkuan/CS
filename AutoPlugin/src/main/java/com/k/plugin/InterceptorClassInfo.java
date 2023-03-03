package com.k.plugin;

public class InterceptorClassInfo {
    public final String className;
    public final int priority;
    public final String name;

    public InterceptorClassInfo(String className, int priority, String name) {
        this.className = className.replace('/', '.');
        ;
        this.priority = priority;
        this.name = name;
    }

    @Override
    public String toString() {
        return "className:" + className + " priority:" + priority + " name:" + name;
    }
}
