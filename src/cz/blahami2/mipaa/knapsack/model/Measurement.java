/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 *
 * @author MBlaha
 */
public class Measurement {

    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private long currentTime = 0;
    private long measuredTime = 0;

    public Measurement() {
        if ( threadMXBean.isCurrentThreadCpuTimeSupported() ) {
            threadMXBean.setThreadContentionMonitoringEnabled( true );
            threadMXBean.setThreadCpuTimeEnabled( true );
        }
    }

    public long start() {
        currentTime = getCurrentTime();
//        currentTime = threadMXBean.getCurrentThreadCpuTime();
//        System.out.println( "clock start: " + currentTime );
        return currentTime;
    }

    public long stop() {
        long newCurrentTime = getCurrentTime();
//        long newCurrentTime = threadMXBean.getCurrentThreadCpuTime();
        measuredTime = newCurrentTime - currentTime;
//        System.out.println( "clock end: " + newCurrentTime );
//        for ( int i = 0; i < 1000; i++ ) {
//            System.out.println( "control measurement change: " + threadMXBean.getCurrentThreadUserTime() );
//        }
        return measuredTime;
    }

    public long getMeasuredTime() {
        return measuredTime;
    }

    private long getCurrentTime() {
        return System.nanoTime();
//        return threadMXBean.isCurrentThreadCpuTimeSupported()
//               ? threadMXBean.getCurrentThreadCpuTime() : System.nanoTime();
    }
}
