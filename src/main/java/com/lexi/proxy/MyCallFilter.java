package com.lexi.proxy;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

public class MyCallFilter implements CallbackFilter {
    @Override
    public int accept(Method method) {
        if(method.getName().equals("toString")||method.getName().equals("hashCode"))
        {
            return 0;
        }else
        {
            return 1;
        }
    }
}
