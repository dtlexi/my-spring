package com.lexi;

import com.lexi.factory.BeanFactory;
import com.lexi.factory.BeanFactory1;
import com.lexi.service.A;
import com.lexi.service.B;
import com.lexi.service.C;

public class Demo1 {
	public static void main(String[] args) throws Exception {
		BeanFactory beanFactory=new BeanFactory();

		System.out.println(beanFactory.getBean(A.class));
		System.out.println(beanFactory.getBean(C.class).getA());
		System.out.println(beanFactory.getBean(B.class).getA());
	}
}
