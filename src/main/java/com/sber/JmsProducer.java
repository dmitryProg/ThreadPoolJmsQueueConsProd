package com.sber;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.sber.Main.DELAY_PRODUCER_CLOSE_MS;

@Slf4j
public class JmsProducer extends Thread implements AutoCloseable {

    private static String DEF_QUEUE = "test.in";
    private final ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Queue<String> messagesQueue;
    private boolean active = true;

    public JmsProducer(String url) {
        connectionFactory = new ActiveMQConnectionFactory(url);
        messagesQueue = new PriorityBlockingQueue<String>();
    }

    private MessageProducer init() throws JMSException {
        log.info("\n Init producer...");
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = session.createQueue(DEF_QUEUE);
        log.info("Producer successfully initialized");
        return session.createProducer(dest);
    }

    public void send(String line) {
        messagesQueue.add(line);
    }

    @Override
    public void run() {
        try {
            MessageProducer messageProducer = init();
            log.info("Producer is running");

            while (active) {
                try {
                    String text;
                        while (active && (text = messagesQueue.poll()) != null) {
                            Message msg = session.createTextMessage(text);
                            msg.setObjectProperty("Created", (new Date()).toString());
                            //log.info("inside sending " + ((TextMessage) msg).getText());
                            messageProducer.send(msg);
                            log.info("Message |" + ((TextMessage) msg).getText()  +
                                    "| with ID " + msg.getJMSMessageID());
                    }
                } catch (JMSException e) {
                    e.printStackTrace();//todo
                    session.close();
                    connection.close();
                    messageProducer = init();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();//todo
        }

    }

    public void close() {
        try {
            //exception without it,
            //resource is not re-opened
            TimeUnit.MILLISECONDS.sleep(DELAY_PRODUCER_CLOSE_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("AutoClosing producer and connection");
        //active = false;
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
