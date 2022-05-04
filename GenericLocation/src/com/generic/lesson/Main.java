package com.generic.lesson;

public class Main {
	public static void main(String[] args) {
		List list = new List();
		System.out.println("Current count: " + list.getCount());

		list.add(0);
		int item = list.get(0);

		System.out.println("Current count: " + list.getCount());
		System.out.print(item);
	}
}
