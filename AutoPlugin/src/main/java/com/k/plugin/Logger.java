package com.k.plugin;

import org.gradle.api.Project;
import org.gradle.api.logging.LogLevel;

public class Logger {
    static Project project;
    public static void error(String msg){
        project.getLogger().error(msg);
    }

    public static void info(String msg){
        project.getLogger().log(LogLevel.WARN,msg);
    }

}
