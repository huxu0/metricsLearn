package com.huxu.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CountersExample {

    //定义注册表
    public static final MetricRegistry registry = new MetricRegistry();
    //定义报告打印
    public static final ConsoleReporter report = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.MINUTES)
            .convertDurationsTo(TimeUnit.MINUTES)
            .build();

    public static final BlockingQueue<Long> queue = new LinkedBlockingQueue<Long>(1000);

    public static void main(String[] args) {

        Counter counter = registry.counter("queue-counter");
        //设置报告打印时间
        report.start(1, TimeUnit.SECONDS);

        new Thread(()->{
            for ( ; ; ){
                randomSleep();
                queue.add(System.nanoTime());
                counter.inc();
            }
        }).start();

        new Thread(()->{
            for ( ; ; ){
                randomSleep();
                if (queue.poll() != null){
                    counter.dec();
                };
            }
        }).start();
    }

    public static void randomSleep(){
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
