package com.iptiq.assessment.consumer;

import com.google.common.collect.MinMaxPriorityQueue;
import com.iptiq.assessment.PriorityProcess;
import com.iptiq.assessment.Process;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class PriorityListConsumer implements Runnable {

    final MinMaxPriorityQueue<PriorityProcess> queue;
    final List<PriorityProcess> list;

    public PriorityListConsumer(MinMaxPriorityQueue<PriorityProcess> queue, List<PriorityProcess> list) {
        this.queue = queue;
        this.list = list;
    }

    public void run() {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            PriorityProcess process = queue.poll();
            if (process == null){
                break;
            } else {
                list.add(process);
            }
        }
    }
}
