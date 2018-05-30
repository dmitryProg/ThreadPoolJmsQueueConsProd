package com.sber;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ProducerService {
    private ExecutorService producerExecutorService;

    public ProducerService() {
        producerExecutorService = Executors//.newCachedThreadPool();
                .newFixedThreadPool(10);
    }

    Runnable produceTask = () -> {
        try {
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = "tcp://localhost:61616";
        try (JmsProducer producer = new JmsProducer(url)) {
            producer.start();

            String line = "first";
            log.info("Before producer actions the queue contains:");
            log.info("||| " + Main.linkedBlockingQueue.toString() + " |||");
            while (line != null) {
                line = Main.linkedBlockingQueue.poll();
                log.info("Producer line of PS is " + line);
                producer.send(System.currentTimeMillis() + line);
            }
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    };

    public void start(){
        producerExecutorService.submit(produceTask);
    }
}
