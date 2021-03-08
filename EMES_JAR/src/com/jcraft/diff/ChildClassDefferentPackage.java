package com.jcraft.diff;

import java.util.Arrays;

/**
 * 
 * 
 * Bitwise AND & op1 & op2 Bitwise exclusive OR ^ op1 ^ op2 Bitwise inclusive OR
 * | op1 | op2 Bitwise Compliment ~ ~ op Bitwise left shift << op1 << op2
 * Bitwise right shift >> op1 >> op2 Unsigned Right Shift Operator >>> op >>>
 * number of places to shift
 *
 */
public class ChildClassDefferentPackage {

	public static void go() {
		int newCapacity = 11 + (11 >> 1);
		System.out.println(newCapacity);
	}

	public static void main(String[] args) {
		go5();
	}

	void check() {

		int a = 60; /* 60 = 0011 1100 */
		int b = 13; /* 13 = 0000 1101 */
		int c = 0;

		c = a & b; /* Bit Wise AND 12 = 0000 1100 */
		System.out.println("a & b = " + c);

		c = a | b; /* 61 = 0011 1101 */
		System.out.println("a | b = " + c);

		c = a ^ b; /* Bit Wise exclusive OR 49 = 0011 0001 */
		System.out.println("a ^ b = " + c);

		c = ~a; /* Bitwise Compliment -61 = 1100 0011 */
		System.out.println("~a = " + c);

		c = a << 2; /* Bitwise left shift 240 = 1111 0000 */
		System.out.println("a << 2 = " + c);

		c = a >> 2; /* Bitwise right shift 15 = 1111 */
		System.out.println("a >> 2  = " + c);

		c = a >>> 2; /* 15 = 0000 1111 */
		System.out.println("a >>> 2 = " + c);
	}

	static void go2() {
		Object[] obj = { "saa", "vaa", "taa" };
		System.out.println(obj.length);
		obj = Arrays.copyOf(obj, 10);
		System.out.println(obj.length);
	}

	/*
	 * There are two array objects of int type. one is containing 100 elements and
	 * another one is containing 10 elements. Can you assign array of 100 elements
	 * to an array of 10 elements?
	 */
	static void go3() {
		// type of the array should be same
		int[] a = new int[10];
		int[] b = new int[100];
		float[] d = new float[100];
		double[] c = new double[10];
		a = b;
		System.out.println(a.length);
	}
	
	/**
	 * copy of the array exaple using loop
	 */
	static void go4() {
		// type of the array should be same
		String[] a = {"AAA","EEE","BBB","FFF","CCC","HHH","DDD","GGG","III"};
		Arrays.sort(a);
		System.out.println(a.length);
		String[] b = new String[a.length];
		for(int i=0;i<a.length;i++) {
			System.out.println(a[i]);
			b[i]=a[i];			
		}
		System.out.println(b.toString());
	}

	/**
	 * copy of the array exaple using loop
	 */
	static void go5() {
		// type of the array should be same
		String[] a = {"AAA","EEE","BBB","FFF","CCC","HHH","DDD","GGG","CCC"};
	
		for(int i=0;i<a.length;i++) {
			System.out.print(a[i]+",");
		}
	}


}
