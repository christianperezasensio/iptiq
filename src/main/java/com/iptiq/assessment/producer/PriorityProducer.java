package com.iptiq.assessment.producer;

import com.google.common.collect.MinMaxPriorityQueue;
import com.iptiq.assessment.PriorityProcess;

public class PriorityProducer implements Runnable {

    final MinMaxPriorityQueue<PriorityProcess> queue;
    final PriorityProcess process;

    public PriorityProducer(MinMaxPriorityQueue<PriorityProcess> queue, PriorityProcess process) {
        this.queue = queue;
        this.process = process;
    }

    @Override
    public void run() {
        queue.add(process);
    }
}
