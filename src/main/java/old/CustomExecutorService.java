package old;

import com.sber.JmsConsumer;
import com.sber.JmsProducer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CustomExecutorService {
    //        Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
//        System.out.println("Number of currently running threads: " + threads.size());
//        for (Thread thread : threads.keySet()) {
//            System.out.println(thread);
//        }

//
//    private BlockingQueue<Callable<String>> innerQueue;//todo;
//    private ExecutorService executorService;
//    private final static String URL = "tcp://localhost:61616";
//
//    public CustomExecutorService() {
//        innerQueue = new LinkedBlockingQueue<Callable<String>>();
//        executorService = Executors//.newCachedThreadPool();
//                                    .newFixedThreadPool(10);
//    }
//
//    private void assignTasks() {
//        try {
//            log.info("Assigning produce task");
//            innerQueue.offer(produceTask, 1000, TimeUnit.MILLISECONDS);
//            log.info("Assigning consume task");
//            innerQueue.offer(consumeTask, 3000, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();//todo
//        }
//    }
//
//    public void startExecutor() {
//            log.info("Assigning tasks...");
//            assignTasks();
//        try {
//            log.info("awaiting...");
//            executorService.invokeAll(innerQueue);
//
////
////            log.info("Trying to submit prod task");
////            Future<String> prod = executorService.submit(produceTask);
////            TimeUnit.MILLISECONDS.sleep(5000);
////            log.info("Trying to submit cons task");
////            Future<String> cons = executorService.submit(consumeTask);
////            TimeUnit.MILLISECONDS.sleep(2000);
////            try {
////                log.info("prod: " + prod.get());
////                log.info("cons: " + cons.get());
////            } catch (ExecutionException e) {
////                e.printStackTrace();//todo
////            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();//todo
//        }
//        executorService.shutdown();
//        log.info("Shut down |" + executorService.isShutdown() + "| and Terminated |" + executorService.isTerminated());
//        executorService.shutdownNow();
//        log.info("Shut down |" + executorService.isShutdown() + "| and Terminated |" + executorService.isTerminated());
//    }
//
//    private Callable<String> produceTask = () -> {
//        String inputLine = "";
//        try (JmsProducer producer = new JmsProducer(URL)) {
//            producer.start();
//            log.info("Initialized producer from thread: " + Thread.currentThread().getName());
//
//            //while(!...){producer.send();}
//            //String might be not the best way, StringBuilder todo
//
//            inputLine = String.valueOf(System.currentTimeMillis());
//            inputLine = inputLine + " thread: " + Thread.currentThread().getName();
//            log.info("Message sent in executor: " + inputLine);
//
//            producer.send(inputLine);
//        }
//        log.info("end of produceTask");
//        return inputLine;
//    };
//
//    private Callable<String> consumeTask = () -> {
//        TimeUnit.MILLISECONDS.sleep(100);
//        String lastMessage = "initial message";
//        try (JmsConsumer consumer = new JmsConsumer(URL, "test.in")) {
//            consumer.init();
//            lastMessage = consumer.getLastMessage();
//            log.info("Initialized consumer from thread: " + Thread.currentThread().getName());
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();//todo
//        }
//        log.info("end of consumeTask");
//        return lastMessage;
//    };

}

////            List<Future<Message>> messageList =
////                    executorService.invokeAll(synchronousInnerQueue);
////            log.info("invoked " + messageList.size());
////            for (Future<Message> messageFuture : messageList) {
////                try {
////                    log.info("getting message");
////                    log.info(String.valueOf(messageFuture.get()));
////                } catch (ExecutionException e) {
////                    e.printStackTrace();//todo
////                }
////            }