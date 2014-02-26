package com.ddosd.facade;

import java.util.PriorityQueue;

public class BuffredThreadQueue {
	
	public static PriorityQueue<BuffredThread> buffredThreads=new PriorityQueue<BuffredThread>(1,new BuffredThredComparator());

}
