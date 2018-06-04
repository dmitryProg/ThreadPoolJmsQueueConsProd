package com.sber;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sber.Main.DELAY_CONSUMER_CLOSE_MS;

@Slf4j
public class JmsConsumer extends Thread implements AutoCloseable {

    private final ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    private String queueName;
    private List<String> messages = new ArrayList<>();
    private boolean isRunning = true;
    private InnerQueueSingleton innerQueueSingleton;

    public JmsConsumer(String url, String queue) {
        connectionFactory = new ActiveMQConnectionFactory(url);
        queueName = queue;
        innerQueueSingleton = InnerQueueSingleton.getInstance();
    }

    public MessageConsumer init() throws JMSException {
        log.info("\n Init consumer...");
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = session.createQueue(queueName);
        consumer = session.createConsumer(dest);
        log.info("Consumer successfully initialized");
        return consumer;
    }

    @Override
    public void run() {
        MessageConsumer messageConsumer = null;
        try {
            messageConsumer = init();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        log.info("Consumer is running \n");

        while (isRunning) {
            Message msg = null;
            try {
                if (messageConsumer != null) {
                    msg = messageConsumer.receive();
                } else {
                    log.error("EMPTY CONSUMER");
                }
            } catch (JMSException e) {
                e.printStackTrace();//todo
            }
            if (msg instanceof TextMessage) {
                try {
                    String lastMessage = ((TextMessage) msg).getText();
                    log.info("Received message: " + ((TextMessage) msg).getText());
                    innerQueueSingleton.offerElement(lastMessage);
                } catch (JMSException e) {
                    e.printStackTrace();//todo
                    try {
                        session.close();
                        connection.close();
                        consumer = init();
                    } catch (JMSException e1) {
                        e1.printStackTrace();
                    }
                }
            } else log.info("Received message: " + msg.getClass().getName());
        }
    }

    public void close() {
        try {
            TimeUnit.MILLISECONDS.sleep(DELAY_CONSUMER_CLOSE_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("AutoClosing consumer and session&connection");
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
