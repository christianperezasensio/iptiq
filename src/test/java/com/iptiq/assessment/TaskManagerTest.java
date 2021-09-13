package com.iptiq.assessment;

import com.iptiq.assessment.producer.FillerProducer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TaskManagerTest {

    private static final int BOUND = 100;
    private static final AtomicInteger lastId = new AtomicInteger();

    @SneakyThrows
    @Test
    void addition_success() {
        BlockingQueue<Process> queue = new ArrayBlockingQueue<>(BOUND);

        TaskManager.add(queue, new Process(lastId.incrementAndGet()));
        Thread.sleep(1000);
        assertEquals(1, queue.size());
    }

    @SneakyThrows
    @Test
    void addition_fail() {
        BlockingQueue<Process> queue = new ArrayBlockingQueue<>(BOUND);

        fillQueue(queue);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());

        Process process = new Process(lastId.incrementAndGet());
        TaskManager.add(queue, process);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());
        assertFalse(queue.contains(process));
    }

    @SneakyThrows
    @Test
    void list() {
        BlockingQueue<Process> queue = new ArrayBlockingQueue<>(BOUND);
        List<Process> list = new ArrayList<>();

        fillQueue(queue);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());

        TaskManager.list(queue, list);
        Thread.sleep(1000);
        assertEquals(0, queue.size());
        assertEquals(BOUND, list.size());
        assertEquals(1, list.get(0).getId());
    }

    @SneakyThrows
    @Test
    void kill_all() {
        BlockingQueue<Process> queue = new ArrayBlockingQueue<>(BOUND);

        fillQueue(queue);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());

        TaskManager.killAll(queue);
        Thread.sleep(1000);
        assertEquals(0, queue.size());
    }

    @SneakyThrows
    @Test
    void kill() {
        BlockingQueue<Process> queue = new ArrayBlockingQueue<>(BOUND);
        Process process = new Process(lastId.incrementAndGet());

        TaskManager.add(queue, process);
        Thread.sleep(1000);
        assertEquals(1, queue.size());

        TaskManager.kill(queue, process);
        Thread.sleep(1000);
        assertEquals(0, queue.size());
    }

    private void fillQueue(BlockingQueue<Process> queue) {
        FillerProducer producer = new FillerProducer(queue, BOUND);
        new Thread(producer).start();
    }
}
