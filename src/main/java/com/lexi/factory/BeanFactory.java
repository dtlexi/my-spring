package com.lexi.factory;

import com.lexi.proxy.CglibProxyFactory;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class BeanFactory {
	private final Map<String,Object> singletonObjects;
	private final Map<String,Object> earlySingletonObjects;

	public BeanFactory()
	{
		this.singletonObjects=new HashMap<String, Object>();
		this.earlySingletonObjects=new HashMap<String, Object>();
	}

	// 创建对象
	private  <T> T createBean(String beanName,Class<T> beanCls) throws Exception {
		Constructor constructor=beanCls.getDeclaredConstructor();
		T bean= (T)constructor.newInstance();

		earlySingletonObjects.put(beanName,bean);

		this.populateBean(bean);

		// 判断当前是否需要aop,我们这边写死
		boolean warpIfNecessary=true;
		if(warpIfNecessary)
		{
			bean= (T) this.wrapInstance(bean);
		}

		return bean;
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

	// aop
	private Object wrapInstance(Object obj)
	{
		CglibProxyFactory proxyFactory=new CglibProxyFactory(obj);
		return proxyFactory.createProxyInstance();
	}

	private Object getSingleton(String beanName)
	{
		Object obj=this.singletonObjects.get(beanName);
		if(obj==null)
		{
			obj=this.earlySingletonObjects.get(beanName);
		}
		return  obj;
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
		}
	}
}
