package com.lexi;

import com.lexi.factory.BeanFactory;
import com.lexi.factory.BeanFactory2;
import com.lexi.service.A;
import com.lexi.service.B;
import com.lexi.service.C;

public class Demo1 {
	public static void main(String[] args) throws Exception {
		BeanFactory2 beanFactory=new BeanFactory2();
		System.out.println(beanFactory.getBean(A.class));
		System.out.println(beanFactory.getBean(B.class).getA());
//		System.out.println(beanFactory.getBean(C.class).getA());
	}
}
