package com.sber;

import lombok.Getter;

import java.util.concurrent.LinkedBlockingQueue;

public class InnerQueueSingleton {
    @Getter
    private LinkedBlockingQueue<String> innerQueue = new LinkedBlockingQueue<>();

    private static volatile InnerQueueSingleton innerQueueSingletonInstance;

    public static InnerQueueSingleton getInstance() {
        InnerQueueSingleton localInstance = innerQueueSingletonInstance;
        if (localInstance == null) {
            synchronized (InnerQueueSingleton.class) {
                localInstance = innerQueueSingletonInstance;
                if (localInstance == null) {
                    innerQueueSingletonInstance = localInstance = new InnerQueueSingleton();
                }
            }
        }
        return localInstance;
    }


    public String pollElement() {
        return innerQueue.poll();
    }

    public void offerElement(String element) {
        this.innerQueue.offer(element);
    }

    public boolean isEmpty() {
        return innerQueue.isEmpty();
    }
}
