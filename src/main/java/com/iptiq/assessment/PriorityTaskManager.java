package com.iptiq.assessment;

import com.google.common.collect.MinMaxPriorityQueue;
import com.iptiq.assessment.consumer.ListConsumer;
import com.iptiq.assessment.consumer.PriorityListConsumer;
import com.iptiq.assessment.producer.PriorityProducer;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class PriorityTaskManager {

    private static final int BOUND = 100;

    public static void main(String[] args) {
        BlockingQueue<PriorityProcess> queue = new BoundedPriorityBlockingQueue<>(BOUND);
    }

    protected static void add(MinMaxPriorityQueue<PriorityProcess> queue, PriorityProcess process) {
        PriorityProducer producer = new PriorityProducer(queue, process);
        new Thread(producer).start();
    }

    protected static void list(MinMaxPriorityQueue<PriorityProcess> queue, List<PriorityProcess> list) {
        PriorityListConsumer consumer = new PriorityListConsumer(queue, list);
        new Thread(consumer).start();
    }
}
