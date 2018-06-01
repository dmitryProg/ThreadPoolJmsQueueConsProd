package com.sber;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ProducerService {
    private ExecutorService producerExecutorService;

    public ProducerService() {
        producerExecutorService = Executors//.newCachedThreadPool();
                .newFixedThreadPool(4);
    }

    Runnable produceNotDelayedTask = () -> {
        String url = "tcp://localhost:61616";
        try (JmsProducer producer = new JmsProducer(url)) {
            producer.start();

            String line;
            log.info("Before producer actions the queue contains: \n");
            log.info("||| " + Main.linkedBlockingQueue.toString() + " |||");
            while (!Main.linkedBlockingQueue.isEmpty()) {
                //Thread.sleep(100);
                line = Main.linkedBlockingQueue.poll();
                log.info("Producer line of PS is " + line);
                producer.send(line + " -N");
            }
            //TimeUnit.MILLISECONDS.sleep(200);
            log.info("End of produceTask");
        } catch (Throwable e) {
            e.printStackTrace();//todo change all these exceptions
        }
    };

    public void start() {
        try {
            Thread.sleep(100);
            producerExecutorService.submit(produceNotDelayedTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
