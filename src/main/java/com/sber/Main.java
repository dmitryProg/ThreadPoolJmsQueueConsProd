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

        linkedBlockingQueue.offer("1");
        linkedBlockingQueue.offer("2");
        linkedBlockingQueue.offer("3");

        try {
            TimeUnit.MILLISECONDS.sleep(100);
            producerService.start();
            TimeUnit.MILLISECONDS.sleep(5000);
            consumerService.start();

            for (int i = 0; i < 3; i++) {
                producerService.start();
                TimeUnit.MILLISECONDS.sleep(1000);
//                consumerService.start();
            }
            producerService.start();

            TimeUnit.MILLISECONDS.sleep(20000);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
