package com.iptiq.assessment;

import com.google.common.collect.MinMaxPriorityQueue;
import com.iptiq.assessment.producer.PriorityFillerProducer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.iptiq.assessment.Priority.HIGH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PriorityTaskManagerTest {

    private static final int BOUND = 100;
    private static final AtomicInteger lastId = new AtomicInteger();

    @SneakyThrows
    @Test
    void default_addition_success() {
        MinMaxPriorityQueue<PriorityProcess> queue = MinMaxPriorityQueue
                .orderedBy(Comparator.comparing(PriorityProcess::getPriority))
                .maximumSize(BOUND)
                .create();

        PriorityTaskManager.add(queue, new PriorityProcess(lastId.incrementAndGet(), Priority.LOW));
        Thread.sleep(1000);
        assertEquals(1, queue.size());
    }

    @SneakyThrows
    @Test
    void priority_addition() {
        MinMaxPriorityQueue<PriorityProcess> queue = MinMaxPriorityQueue
                .orderedBy(Comparator.comparing(PriorityProcess::getPriority))
                .maximumSize(BOUND)
                .create();

        fillQueue(queue);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());

        PriorityProcess process = new PriorityProcess(lastId.incrementAndGet(), HIGH);
        PriorityTaskManager.add(queue, process);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());
        assertTrue(queue.contains(process));
        assertEquals(process, queue.peekFirst());
    }

    @SneakyThrows
    @Test
    void list() {
        MinMaxPriorityQueue<PriorityProcess> queue = MinMaxPriorityQueue
                .orderedBy(Comparator.comparing(PriorityProcess::getPriority))
                .maximumSize(BOUND)
                .create();
        List<PriorityProcess> list = new ArrayList<>();

        fillQueue(queue);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());

        PriorityProcess process = new PriorityProcess(lastId.incrementAndGet(), HIGH);
        PriorityTaskManager.add(queue, process);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());
        assertTrue(queue.contains(process));
        assertEquals(process, queue.peekFirst());

        PriorityTaskManager.list(queue, list);
        Thread.sleep(1000);
        assertEquals(0, queue.size());
        assertEquals(BOUND, list.size());
        assertEquals(1, list.get(0).getId());
        assertEquals(HIGH, list.get(0).getPriority());
    }

    private void fillQueue(MinMaxPriorityQueue<PriorityProcess> queue) {
        PriorityFillerProducer producer = new PriorityFillerProducer(queue, BOUND);
        new Thread(producer).start();
    }
}
