package com.huxu.metrics.metric;

import com.codahale.metrics.CachedGauge;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;

import java.util.concurrent.TimeUnit;

public class CacheGaugeExample {

    private static final MetricRegistry metrics = new MetricRegistry();

    public static void main(String[] args) throws InterruptedException {

        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.MINUTES)
                .convertDurationsTo(TimeUnit.MINUTES)
                .build();
        reporter.start(10, TimeUnit.SECONDS);
        // 定义Ratio Gauge 并且注册到metrics上
        metrics.register(MetricRegistry.name(CacheGaugeExample.class, "CacheGauge"), new CachedGauge<Long>(10, TimeUnit.MINUTES){

            @Override
            protected Long loadValue() {
                return queryFromHere();
            }
        });

        Thread.currentThread().join();

    }

    private static long queryFromHere(){
        System.out.println("从queryFromHere取的值");
        return System.currentTimeMillis();
    }
}
