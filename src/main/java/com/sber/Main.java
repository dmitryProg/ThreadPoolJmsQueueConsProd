package com.sber;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
    public final static int DELAY_PRODUCER_CLOSE_MS = 60000;
    public final static int DELAY_CONSUMER_CLOSE_MS = 50000;
    public final static int SYSTEM_EXIT_MS = 10000;

    public static void main(String[] args) {
        ConsumerService consumerService = new ConsumerService();
        ProducerService producerService = new ProducerService();
        InnerQueueSingleton innerQueueSingleton = InnerQueueSingleton.getInstance();

        innerQueueSingleton.offerElement("1");
        innerQueueSingleton.offerElement("2");
        innerQueueSingleton.offerElement("3");

        try {
            TimeUnit.MILLISECONDS.sleep(100);
            producerService.start();
            TimeUnit.MILLISECONDS.sleep(2000);
            consumerService.start();

            for (int i = 0; i < 3; i++) {
                producerService.start();
                TimeUnit.MILLISECONDS.sleep(1000);
            }

            TimeUnit.MILLISECONDS.sleep(SYSTEM_EXIT_MS);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
