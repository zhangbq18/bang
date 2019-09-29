package com.zhangbq.testlib;


import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by zyw on 2016/7/13.
 */
public class DealQueue extends Thread {

    private Broker broker;
    final Object lock = new Object();

    public DealQueue() {
        this.setPriority(Thread.MIN_PRIORITY);
        this.setDaemon(true);
        broker = new Broker();
        broker.setonHandleMsgCallListener(new HandleMsgCallListener() {
            @Override
            public void handleMessage(String msg) {
                msg = msg.replace("主线程", "子线程");
                System.out.println("消费消息：--->>" + msg);
                dealReadTask(msg);
            }
        });
    }

    @Override
    public void run() {
        while (true) {
            broker.consume();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public  void dealReadTask(String msg) {
        System.out.println("进入同步块之前-->>"+ msg);
        synchronized(lock){
            System.out.println(Thread.currentThread().getName() +"同步块start-->>"+ msg);
            try {
                for(int i = 0;i<100000;i++) {
                    Broker broker = new Broker();
                }
                System.out.println(Thread.currentThread().getName() +"同步块end-->>"+ msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dealAsync(String msg) {
        broker.produce(msg);
    }


}

/**
 * 消息处理中心
 */
 class Broker {
    // 队列存储消息的最大数量
    private final static int MAX_SIZE = 100;
    HandleMsgCallListener listener1;
    // 保存消息数据的容器
    private ArrayBlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(MAX_SIZE);
    public void setonHandleMsgCallListener(HandleMsgCallListener listener) {
        listener1 = listener;
    }

    // 生产消息
    public void produce(String msg) {
        if (messageQueue.offer(msg)) {
            System.out.println("产生消息：--->>" + msg);
        } else {
            System.out.println("消息处理中心内暂存的消息达到最大负荷，不能继续放入消息！");
        }
    }

    // 消费消息
    public String consume() {
        String msg = messageQueue.poll();
        if (msg != null) {
            // 消费条件满足情况，从消息容器中取出一条消息
            listener1.handleMessage(msg);
        } else {
            System.out.println("消息处理中心内没有消息可供消费！");
        }

        return msg;
    }

}

interface HandleMsgCallListener {
    void handleMessage(String msg);
}
