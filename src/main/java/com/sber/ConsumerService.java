package com.sber;

import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class ConsumerService {
    private ExecutorService consumerExecutorService;

    public ConsumerService() {
        consumerExecutorService = Executors//.newCachedThreadPool();
                .newFixedThreadPool(10);
    }

    private Runnable consumeTask = () -> {
        String URL = "tcp://localhost:61616";
        List<String> messages = null;
        try (JmsConsumer consumer = new JmsConsumer(URL, "test.in")) {
            while (messages == null) {
                consumer.init();
                messages = consumer.getMessages();
            }
            Main.linkedBlockingQueue.addAll(messages);
            log.info("Initialized consumer from thread: " + Thread.currentThread().getName());
        } catch (InterruptedException | JMSException e) {
            e.printStackTrace();//todo
        } catch (NullPointerException e) {
            log.error("PLEASE RUN PRODUCER APPLICATION FIRST");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("end of consumeTask");
    };

    public void start(){
        consumerExecutorService.submit(consumeTask);
    }
}
