package com.sber;

import lombok.extern.slf4j.Slf4j;

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
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = "tcp://localhost:61616";
        try (JmsProducer producer = new JmsProducer(url)) {
            producer.start();

            String line;
            log.info("Before producer actions the queue contains:");
            log.info("||| " + Main.linkedBlockingQueue.toString() + " |||");
            while (!Main.linkedBlockingQueue.isEmpty()){
                Thread.sleep(100);
                line = Main.linkedBlockingQueue.poll();
                log.info("Producer line of PS is " + line);
                producer.send(line + " at time: " + System.currentTimeMillis());
            }
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (Throwable e) {
            e.printStackTrace();//todo change all these exceptions, veri govnokods
        }
    };

    public void start() {
        producerExecutorService.submit(produceTask);
    }
}
