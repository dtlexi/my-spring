package com.lexi;

import com.lexi.factory.BeanFactory;
import com.lexi.service.A;
import com.lexi.service.B;

public class Demo1 {
	public static void main(String[] args) throws Exception {
		BeanFactory beanFactory=new BeanFactory();

		System.out.println(beanFactory.getBean(A.class));
		System.out.println(beanFactory.getBean(B.class).getA());
		System.out.println();
		System.out.println(beanFactory.getBean(B.class));
		System.out.println(beanFactory.getBean(A.class).getB());
//		A a=beanFactory.getBean(A.class);
//		A a2=beanFactory.getBean(B.class).getA();
//
//		System.out.println(a);
//		System.out.println(a2);

//		System.out.println(a.getClass());
//		System.out.println(a2.getClass());
//		System.out.println("");
	}
}
