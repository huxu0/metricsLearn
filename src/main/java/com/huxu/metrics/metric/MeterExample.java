package com.huxu.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MeterExample {

    private static final MetricRegistry metrics = new MetricRegistry();
    private static final Meter requests = metrics.meter("tqs");


    public static void main(String[] args) {

        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.MINUTES)
                .convertDurationsTo(TimeUnit.MINUTES)
                .build();
        reporter.start(5, TimeUnit.SECONDS);

        for ( ; ; ){
            handle(null);
            randomSleep();
        }
    }


    public static void handle(byte[] request){
        requests.mark();
        randomSleep();
    }

    public static void randomSleep(){
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
