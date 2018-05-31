package com.sber;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
    public static LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue();

    public static void main(String[] args) {
        ConsumerService consumerService = new ConsumerService();
        ProducerService producerService = new ProducerService();
        consumerService.start();
        producerService.start();
        try {
            TimeUnit.MILLISECONDS.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        consumerService.start();
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



//        Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
//        System.out.println("Number of currently running threads: " + threads.size());
//        for (Thread thread : threads.keySet()) {
//            System.out.println(thread);
//        }
    }
}
