package com.lexi.factory;

import com.lexi.proxy.CglibProxyFactory;
import com.lexi.proxy.ProxyCreator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BeanFactory2 {
    private final Map<String,Object> singletonObjects;
    private final Map<String,Object> earlySingletonObjects;
    private final Map<String, ObjectFactory<?>> singletonFactories;

    private final ProxyCreator proxyCreator;

    public BeanFactory2()
    {
        this.singletonObjects=new HashMap<String, Object>();
        this.earlySingletonObjects=new HashMap<String, Object>();
        this.singletonFactories=new HashMap<String, ObjectFactory<?>>();
        proxyCreator=new ProxyCreator();
    }

    // 创建对象
    private  <T> T createBean(String beanName,Class<T> beanCls) throws Exception {
        Constructor constructor=beanCls.getDeclaredConstructor();
        final T bean= (T)constructor.newInstance();

        // 是否暴露早起引用，spring这边判断的是
        // 1. 当前对象是否是单例
        // 2. 当前对象是否正在创建中，
        // 3. 是否支持循环引用
        // 我们这边就直接简单的都默认为true
        boolean earlySingletonExposure = true;

        if(earlySingletonExposure)
        {
            // addSingletonFactory(beanName,ObjectFactory);
            addSingletonFactory(beanName,()->{
                return this.proxyCreator.getEarlyBeanReference(beanName,bean);
            });
        }
        this.populateBean(bean);

        T exposedObject= (T) this.proxyCreator.wrapInstance(beanName,bean);

        return exposedObject;
    }

    // 属性赋值
    private void populateBean(Object obj) throws Exception {
        Class beanCla=obj.getClass();
        Field[] fields=beanCla.getDeclaredFields();

        for (Field field:
                fields) {
            Class typeField=field.getType();
            Object fieldBean=getBean(typeField);
            field.setAccessible(true);
            field.set(obj,fieldBean);
        }
    }


    private Object getSingleton(String beanName) throws Exception {
        Object singletonObject=this.singletonObjects.get(beanName);
        if(singletonObject==null)
        {
            singletonObject = this.earlySingletonObjects.get(beanName);
            if(singletonObject==null)
            {
                ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }
            }
        }
        return  singletonObject;
    }

    //获取对象
    public  <T> T getBean(Class<T> cls) throws Exception {
        String beanName=cls.toString();
        Object singletonObject=getSingleton(beanName);
        if(singletonObject!=null)
        {
            return (T)singletonObject;
        }
        singletonObject= createBean(beanName,cls);
        this.addSingleton(beanName,singletonObject);
        return (T) singletonObject;
    }

    private void addSingleton(String beanName, Object singletonObject) {
        this.singletonObjects.put(beanName, singletonObject);
        if(this.earlySingletonObjects.containsKey(beanName))
        {
            this.earlySingletonObjects.remove(beanName);
            this.singletonFactories.remove(beanName);
        }
    }

    private void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
        }
    }
}
