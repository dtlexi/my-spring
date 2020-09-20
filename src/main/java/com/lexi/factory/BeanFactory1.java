package com.lexi.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

public class BeanFactory1 {
    private HashMap<String,Object> singletonObjects;

    public BeanFactory1()
    {
        this.singletonObjects=new HashMap<String, Object>();
    }

    // 创建对象
    private  <T> T createBean(String beanName,Class<T> beanCls) throws Exception {
        Constructor constructor=beanCls.getDeclaredConstructor();
        T bean= (T)constructor.newInstance();
        this.populateBean(bean);
        return bean;
    }

    // 属性赋值
    private void populateBean(Object obj) throws Exception {
        Class beanCla=obj.getClass();
        Field[] fields=beanCla.getDeclaredFields();

        for (Field field:
                fields) {
            Class fieldType=field.getType();
            Object fieldBean=getBean(fieldType);
            field.set(obj,fieldBean);
        }
    }

    public Object getSingleton(String beanName)
    {
        Object obj=this.singletonObjects.get(beanName);
        return  obj;
    }

    /**
     * 获取对象
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
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
    }
}
