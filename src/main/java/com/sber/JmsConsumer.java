package com.sber;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JmsConsumer implements MessageListener, AutoCloseable {

    private final ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    private String queueName;
    private List<String> messages = new ArrayList<>();

    public List<String> getMessages() {
        return messages;
    }

    public JmsConsumer(String url, String queue) {
        connectionFactory = new ActiveMQConnectionFactory(url);
        queueName = queue;
    }

    public void init() throws JMSException {
        log.info("Init consumer...");
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = session.createQueue(queueName);
        consumer = session.createConsumer(dest);
        consumer.setMessageListener(this);
        log.info("Consumer successfully initialized");
    }

    public void onMessage(Message msg) {
        if (msg instanceof TextMessage) {
            try {
                String lastMessage = ((TextMessage)msg).getText();
                log.info("Received message: " + ((TextMessage) msg).getText());
                log.info("LASTMESSAGE " + lastMessage);
                messages.add(lastMessage);
            } catch (JMSException e) {
                e.printStackTrace();//todo
            }
        } else log.info("Received message: " + msg.getClass().getName());
    }

    public void close() throws Exception {
        log.info("AutoClosing consumer and session&connection");
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
