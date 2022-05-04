package com.generic.lesson;

public class List {
	private int[] items = new int[10];
	
	private int count = 0;
	
	public void add(int item) {
		items[count++] = item;
	}
	
	public int get(int index) {
		return items[index];
	}
	
	public int getCount() {
		return this.count;
	}
}
