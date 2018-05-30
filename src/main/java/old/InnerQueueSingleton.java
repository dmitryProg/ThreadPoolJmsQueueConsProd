package old;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InnerQueueSingleton {
    private static InnerQueueSingleton instance = null;//BlockingQueue<Callable<String>> queueSingleton;

    private InnerQueueSingleton() {
    }

    public static InnerQueueSingleton getInstance() {
        if (instance == null) {
            // Thread Safe. Might be costly operation in some case
            synchronized (InnerQueueSingleton.class) {
                if (instance == null) {
                    instance = new InnerQueueSingleton();
                }
            }
        }
        return instance;
    }

}
