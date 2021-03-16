package com.huxu.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxAttributeGauge;
import com.codahale.metrics.MetricRegistry;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.concurrent.TimeUnit;

public class jmxAttributeGuageExample {

    public static final MetricRegistry registry = new MetricRegistry();

    public static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertDurationsTo(TimeUnit.SECONDS)
            .convertRatesTo(TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws MalformedObjectNameException, InterruptedException {
        reporter.start(1,TimeUnit.SECONDS);

        registry.register(MetricRegistry.name(jmxAttributeGuageExample.class, "heapmemory"),
                new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage"));

        Thread.currentThread().join();

    }

}
