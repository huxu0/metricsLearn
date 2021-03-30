package com.huxu.metrics.metric;

import com.codahale.metrics.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class RatioGuageExample {

    private static final MetricRegistry metrics = new MetricRegistry();
    private static final Meter totalMeter = metrics.meter("totalMeter");
    private static final Meter successMeter = metrics.meter("successMeter");

    public static void main(String[] args) {

        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.MINUTES)
                .convertDurationsTo(TimeUnit.MINUTES)
                .build();
        reporter.start(10, TimeUnit.SECONDS);
        // 定义Ratio Gauge 并且注册到metrics上
        metrics.register(MetricRegistry.name(RatioGuageExample.class, "Ratio"), new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(successMeter.getCount(),
                        totalMeter.getCount());
            }
        });
        for ( ; ; ){
            randomSleep();
            business();
        }
    }

    public static void business(){
        totalMeter.mark();
        try{
            int x = 1/ ThreadLocalRandom.current().nextInt(2);
            successMeter.mark();
        }catch (Exception e){
            System.out.println("ERROR");
        }
    }

    public static void randomSleep(){
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
