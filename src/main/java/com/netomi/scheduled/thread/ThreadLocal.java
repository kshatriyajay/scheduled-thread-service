package com.netomi.scheduled.thread;

public class ThreadLocal implements Runnable {
    private int count;
    private String visitorId;
    private int waitTime;
    public ThreadLocal(int count, String visitorId,int waitTime) {
        this.count = count;
        this.visitorId = visitorId;
        this.waitTime = waitTime;
    }
    @Override
    public void run() {
        System.out.println(String.format("Data Returning Visitor Id %s Thread Id %d and processed in %d milli seconds",this.visitorId, this.count, this.waitTime));
    }
}
