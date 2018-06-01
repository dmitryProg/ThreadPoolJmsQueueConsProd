package com.sber;

import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class ConsumerService {
    private ExecutorService consumerExecutorService;

    public ConsumerService() {
        consumerExecutorService = Executors//.newCachedThreadPool();
                .newFixedThreadPool(4);
    }

    private Runnable consumeTask = () -> {
        String URL = "tcp://localhost:61616";
        try (JmsConsumer consumer = new JmsConsumer(URL, "test.in")) {
            consumer.start();
            log.info("End of consumeTask");
        } catch (NullPointerException e) {
            log.error("PLEASE RUN PRODUCER APPLICATION FIRST");
            e.printStackTrace();//todo
        } catch (Exception e) {
            e.printStackTrace();//todo
        }
        log.info("end of consumeTask");
    };

    public void start() {
        consumerExecutorService.submit(consumeTask);
    }
}
