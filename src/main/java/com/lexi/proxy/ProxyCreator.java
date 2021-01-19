package com.lexi.proxy;

import java.util.HashMap;
import java.util.Map;

public class ProxyCreator {
    private final Map<String, Object> earlyProxyReferences;

    public ProxyCreator()
    {
        this.earlyProxyReferences=new HashMap<>();
    }

    public Object getEarlyBeanReference(String beanName,Object bean)
    {
        CglibProxyFactory proxyFactory=new CglibProxyFactory(bean);
        Object warpBean= proxyFactory.createProxyInstance();
        earlyProxyReferences.put(beanName,warpBean);
        return warpBean;
    }

    // aop
    public Object wrapInstance(String beanName,Object bean)
    {
        if(this.earlyProxyReferences.containsKey(beanName))
        {
            return this.earlyProxyReferences.get(beanName);
        }

        CglibProxyFactory proxyFactory=new CglibProxyFactory(bean);
        return proxyFactory.createProxyInstance();
    }
}
