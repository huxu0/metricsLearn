package com.huxu.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.*;

import static com.sun.corba.se.impl.util.RepositoryId.cache;

public class simpleGuageExample {
    //定义注册表
    public static final MetricRegistry registry = new MetricRegistry();
    //定义报告打印
    public static final ConsoleReporter report = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.MINUTES)
            .convertDurationsTo(TimeUnit.MINUTES)
            .build();

    public static final BlockingQueue<Long> queue = new LinkedBlockingQueue<Long>(1000);

    public static void main(String[] args) {

        //注册 定义名字 监控对象
        registry.register(MetricRegistry.name(simpleGuageExample.class, "queue-size"), (Gauge<Integer>) queue :: size);
        //设置报告打印时间
        report.start(1, TimeUnit.SECONDS);

        new Thread(()->{
           for ( ; ; ){
               randomSleep();
               queue.add(System.nanoTime());
           }
        }).start();

        new Thread(()->{
            for ( ; ; ){
                randomSleep();
                queue.poll();
            }
        }).start();
    }

    public static void randomSleep(){
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
