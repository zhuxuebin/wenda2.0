package com.nowcoder;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuery on 2016/8/25.
 */
public class MultiThreadTests {

    public static void main(String[] args) {
        //testThread();
        //testSynchronized();
        testBlockingQueue();
        //testThreadLocal();
        //testExecutor();
        //testAtomic();
        //testFuture();
    }

    public static void testFuture() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                //throw new IllegalArgumentException("异常");
                return 1;
            }
        });

        service.shutdown();
        try {
            System.out.println(future.get());
            //System.out.println(future.get(100, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int counter = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void testWithoutAtomic(){
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for(int j=0;j<10;j++) {
                            counter++;
                            System.out.println(Thread.currentThread().getName()+"::"+counter);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },"thread"+i).start();
        }
    }

    public static void testAtomic(){
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for(int j=0;j<10;j++) {
                            System.out.println(Thread.currentThread().getName()+"::"+atomicInteger.incrementAndGet());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },"thread"+i).start();
        }
    }


    public static void testExecutor(){
        ExecutorService service = Executors.newSingleThreadScheduledExecutor();
        //ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (int i = 0; i < 10; ++i) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor1:" + i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return 0;
            }
        });

        service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (int i = 0; i < 10; ++i) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Executor2:" + i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return 0;
            }
        });
    }


    /**
     * threadLocal用于隔离线程之间的共享变量
     */
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    private static Integer intV = new Integer(10);
    private static int userId;
    public static void testThreadLocal(){
        for(int i=0;i<10;i++){
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        threadLocal.set(finalI);
                        intV = finalI;
                        Thread.sleep(1000);  //关键在这里，所有线程都睡了1s,则打印的时候intV = finalI = 9
                        System.out.println(Thread.currentThread().getName()+":ThreadLocal:"+threadLocal.get());
                        System.out.println(Thread.currentThread().getName()+":notThreadLocal:"+intV);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            },"thread"+i).start();
        }
    }

    public static void testBlockingQueue() {
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q), "Consumer1").start();
        new Thread(new Consumer(q), "Consumer2").start();
    }

    static class Consumer implements Runnable{
        private BlockingQueue<Integer> bq;
        public Consumer(BlockingQueue bq){
            this.bq = bq;
        }

        @Override
        public void run(){
            try{
                while(true){
                    System.out.println(Thread.currentThread().getName()+"::"+bq.take());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    static class Producer implements Runnable{
        private BlockingQueue<Integer> bq;
        public Producer(BlockingQueue bq){
            this.bq = bq;
        }

        @Override
        public void run(){
            try{
                for(int i=0;i<100;i++){
                    Thread.sleep(1000);
                    bq.put(i);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    public static void testThread() {
        for (int i = 0; i < 10; ++i) {
            //new MyThread(i).start();
        }

        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < 10; ++j) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2 %d: %d:", finalI, j));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static Object obj = new Object();

    public static void testSynchronized1() {
        synchronized (obj) {
            try {
                for (int j = 0; j < 10; ++j) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3 %d", j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (new Object()) {
            try {
                for (int j = 0; j < 10; ++j) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4 %d", j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized() {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }
}
