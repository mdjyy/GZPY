package com.gzpy.project.java_project;

import java.util.ArrayList;
import java.util.List;

public class ListRemove {
    public static void main(String[] args) {
		List<Integer> list = new ArrayList();
		list.add(1);
		list.add(1);
		list.add(1);
		list.add(1);
		for(int i=0;i<list.size();i++) {
			if(list.get(i)==1) {
				list.remove(i);
				System.out.println("清楚");
			}
		}
		//结果为 1 0 OK 因为底层是数组，删除数组向前移，而当前指针指向了后一位，所以才会这样
		for(int i=0;i<list.size();i++) {
			System.out.println(list.get(i));
		}
		System.out.println("OK");
	}
}
