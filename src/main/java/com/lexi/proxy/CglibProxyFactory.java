package com.lexi.proxy;

import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;

public class CglibProxyFactory implements MethodInterceptor {

    private Object target;
    public CglibProxyFactory(Object obj)
    {
        this.target=obj;
    }

    public Object createProxyInstance()
    {
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallbackFilter(new MyCallFilter());
        enhancer.setCallbacks(new Callback[]{
                NoOp.INSTANCE,
                this
        });
        return  enhancer.create();
    }

    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = method.invoke(target, objects);
        return result;
    }
}
