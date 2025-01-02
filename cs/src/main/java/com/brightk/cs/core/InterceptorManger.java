package com.brightk.cs.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class InterceptorManger {
    private class InterceptorInfo {
        public final CsInterceptor interceptor;
        public final int priority;
        public final String name;

        public InterceptorInfo(CsInterceptor interceptor, int priority, String name) {
            this.interceptor = interceptor;
            this.priority = priority;
            this.name = name;
        }
    }

    private static volatile InterceptorManger singleton;

    private InterceptorManger() {
    }

    public static InterceptorManger getInstance() {
        if (singleton == null) {
            synchronized (InterceptorManger.class) {
                if (singleton == null) {
                    singleton = new InterceptorManger();
                }
            }
        }
        return singleton;
    }

    private final List<InterceptorInfo> interceptors = Collections.synchronizedList(new ArrayList<>());
    private final List<CsInterceptor> csInterceptors = new ArrayList<>();

    private boolean exist(String name) {
        boolean isExit = false;
        for (InterceptorInfo info : interceptors) {
            if (info.name.equals(name)) {
                isExit = true;
                break;
            }
        }
        return isExit;
    }

    public List<CsInterceptor> getInterceptors() {
        return csInterceptors;
    }


    synchronized public void register(CsInterceptor interceptor, int priority, String name) throws CsException {
        if (exist(name)) {
            throw new CsException(name + "拦截器已经存在");
        }
        InterceptorInfo info = new InterceptorInfo(interceptor, priority, name);
        interceptors.add(info);
        interceptors.sort(Comparator.comparingInt(o -> o.priority));
        csInterceptors.clear();
        interceptors.forEach(interceptorInfo -> csInterceptors.add(interceptorInfo.interceptor));
    }

    synchronized public void unRegister(String name) {
        Iterator<InterceptorInfo> iterator = interceptors.iterator();
        while (iterator.hasNext()) {
            InterceptorInfo info = iterator.next();
            if (info.name.equals(name)) {
                iterator.remove();
                break;
            }
        }
    }
}
