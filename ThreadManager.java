package com.uhrenclan.RPG_Client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadManager {
	private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	
	public void ExecuteOnMainThread(Runnable runnable) {
		if(runnable == null) return;
		queue.add(runnable);
	}
	
	public void UpdateMain(){
		try {
			queue.take().run();
		} catch (InterruptedException e) {
			return;
		}
	}
}
