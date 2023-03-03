package com.brightk.cs;

import com.brightk.cs.core.ComponentServiceManger;
import com.brightk.cs.core.CsException;
import com.brightk.cs.core.CsInterceptor;
import com.brightk.cs.core.InterceptorManger;

import java.lang.reflect.InvocationTargetException;

/**
 * 给插件使用的
 */
public class CsPluginRegister {

    public static void registerService(String key, String className) {
        ComponentServiceManger.pluginRegister(key, className);
    }

    public static void registerInterceptor(String className,int priority,String name){
        try {
            Class<CsInterceptor> interceptorClass = (Class<CsInterceptor>)Class.forName(className);
            CsInterceptor interceptor = interceptorClass.getDeclaredConstructor().newInstance();
            InterceptorManger.getInstance().register(interceptor,priority,name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
           e.printStackTrace();
        } catch (CsException e) {
            e.printStackTrace();
        }

    }
}
