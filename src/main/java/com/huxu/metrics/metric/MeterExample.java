package com.huxu.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MeterExample {

    private static final MetricRegistry metrics = new MetricRegistry();
    private static final Meter requestsMeter = metrics.meter("tqs");
    private static final Meter sizeMeter = metrics.meter("volum");


    public static void main(String[] args) {

        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.MINUTES)
                .convertDurationsTo(TimeUnit.MINUTES)
                .build();
        reporter.start(10, TimeUnit.SECONDS);

        for ( ; ; ){
            handle(new byte[ThreadLocalRandom.current().nextInt(1)]);
            randomSleep();
        }
    }


    public static void handle(byte[] request){
        requestsMeter.mark();
        sizeMeter.mark(request.length);
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
