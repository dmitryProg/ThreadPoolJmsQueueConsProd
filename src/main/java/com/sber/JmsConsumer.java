package com.sber;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JmsConsumer implements MessageListener, AutoCloseable {//MessageListener, extends Thread

    private final ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    private String queueName;
    private List<String> messages = new ArrayList<>();
    private String message;
    private boolean isRunning;

    public List<String> getMessages() {
        //здесь их и добавлять?
        log.info("List of consumer messages: " + messages.toString());
        for (String messageX : messages) {
            Main.linkedBlockingQueue.offer(messageX);
        }
        log.info("Inner QUEUE: " + Main.linkedBlockingQueue.toString());
        return messages;
    }

    public String getMessage() {
        return message;
    }

    public JmsConsumer(String url, String queue) {
        connectionFactory = new ActiveMQConnectionFactory(url);
        queueName = queue;
    }

    public void init() throws JMSException {//MessageConsumer
        log.info("Init consumer...");
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = session.createQueue(queueName);
        consumer = session.createConsumer(dest);
        consumer.setMessageListener(this);
        log.info("Consumer successfully initialized");
        //return consumer;
    }

//    @Override
//    public void run() {
//        MessageConsumer messageConsumer = null;
//        try {
//            messageConsumer = init();
//        } catch (JMSException e) {
//            e.printStackTrace();
//        }
//        log.info("Producer is running");
//
//        while (isRunning) {
//            Message msg = null;
//            try {
//                if (messageConsumer != null) {
//                    msg = messageConsumer.receive(100);
//                }
//                else {
//                    log.error("EMPTY CONSUMER");
//                }
//            } catch (JMSException e) {
//                e.printStackTrace();//todo
//            }
//            if (msg instanceof TextMessage) {
//                try {
//                    String lastMessage = ((TextMessage) msg).getText();
//                    log.info("Received message: " + ((TextMessage) msg).getText());
//                    messages.add(lastMessage);
//                    message = lastMessage;
//                } catch (JMSException e) {
//                    e.printStackTrace();//todo
//                }
//            } else log.info("Received message: " + msg.getClass().getName());
//        }
//    }

    public void onMessage(Message msg) {
        if (msg instanceof TextMessage) {
            try {
                String lastMessage = ((TextMessage) msg).getText();
                log.info("Received message: " + ((TextMessage) msg).getText());
                messages.add(lastMessage);
                message = lastMessage;
            } catch (JMSException e) {
                e.printStackTrace();//todo
            }
        } else log.info("Received message: " + msg.getClass().getName());
    }

    public void close() throws Exception {
        log.info("AutoClosing consumer and session&connection");
        try {
            TimeUnit.MILLISECONDS.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isRunning = false;

        try {
            if (session != null) session.close();
        } catch (JMSException jmsEx) {
            jmsEx.printStackTrace();
        }
        try {
            if (connection != null) connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
