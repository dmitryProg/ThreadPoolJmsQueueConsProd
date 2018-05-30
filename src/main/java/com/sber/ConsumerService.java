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
                .newFixedThreadPool(10);
    }

    private Runnable consumeTask = () -> {
        String URL = "tcp://localhost:61616";
        String message = null;
        List<String> messages = new ArrayList<>();
        try (JmsConsumer consumer = new JmsConsumer(URL, "test.in")) {
            while (message == null) {
                consumer.init();

                messages = consumer.getMessages();//NPE?
                message = consumer.getMessage();
            }
            //Main.linkedBlockingQueue.addAll(messages);
            Main.linkedBlockingQueue.offer(message);
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
