/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iptiq.assessment;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A generic bounded blocking Priority-Queue.
 */
public class BoundedPriorityBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {

    private static final long serialVersionUID = -7659755775266252464L;

    private final PriorityQueue<E> q;
    private final int maxCapacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BoundedPriorityBlockingQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    public BoundedPriorityBlockingQueue(int initialCapacity,
                                        Comparator<? super E> comparator) {
        this(initialCapacity, comparator, Integer.MAX_VALUE);
    }

    public BoundedPriorityBlockingQueue(Collection<? extends E> c) {
        q = new PriorityQueue<E>(c);
        maxCapacity = Integer.MAX_VALUE;
    }

    public BoundedPriorityBlockingQueue(int initialCapacity, int maxCapacity) {
        this(initialCapacity, null, maxCapacity);
    }

    public BoundedPriorityBlockingQueue(int initialCapacity,
                                        Comparator<? super E> comparator, int maxCapacity) {
        q = new PriorityQueue<E>(initialCapacity, comparator);
        this.maxCapacity = maxCapacity;
    }

    public BoundedPriorityBlockingQueue(Collection<? extends E> c, int maxCapacity) {
        q = new PriorityQueue<E>(c);
        this.maxCapacity = maxCapacity;
    }

    @Override
    public Iterator<E> iterator() {
        // TODO - lazy
        lock.lock();
        try {
            return new PriorityQueue<E>(q).iterator();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return q.size();
        } finally {
            lock.unlock();
        }
    }

    public void put(E e) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            try {
                while (q.size() == maxCapacity)
                    notFull.await();
            } catch (InterruptedException ie) {
                notFull.signal();
                throw ie;
            }
            assert offer(e);
        } finally {
            lock.unlock();
        }
    }

    public boolean offer(E e, long l, TimeUnit timeUnit) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            if (q.size() < maxCapacity)
                return offer(e);
            notEmpty.await(l, timeUnit);
            return offer(e);
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            try {
                while (q.size() == 0)
                    notEmpty.await();
            } catch (InterruptedException ie) {
                notEmpty.signal(); // propagate to non-interrupted thread
                throw ie;
            }
            E x = poll();
            assert x != null;
            return x;
        } finally {
            lock.unlock();
        }
    }

    public E poll(long l, TimeUnit timeUnit) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            E x = poll();
            if (x != null)
                return x;
            notEmpty.await(l, timeUnit);
            return poll();
        } finally {
            lock.unlock();
        }
    }

    public int remainingCapacity() {
        lock.lock();
        try {
            return maxCapacity - q.size();
        } finally {
            lock.unlock();
        }
    }

    public int drainTo(Collection<? super E> objects) {
        if (objects == null) {
            throw new NullPointerException();
        }
        if (objects == this) {
            throw new IllegalArgumentException();
        }
        lock.lock();
        try {
            int j = 0; // num object added
            while (size() > 0) {
                objects.add(poll());
                j++;
            }
            return j;
        } finally {
            lock.unlock();
        }
    }

    public int drainTo(Collection<? super E> objects, int i) {
        if (objects == null) {
            throw new NullPointerException();
        }
        if (objects == this) {
            throw new IllegalArgumentException();
        }
        lock.lock();
        try {
            int j = 0; // num object added
            while (size() > 0 && j < i) {
                objects.add(poll());
                j++;
            }
            return j;
        } finally {
            lock.unlock();
        }
    }

    public boolean offer(E e) {
        lock.lock();
        try {
            if (q.size() == maxCapacity)
                return false;
            assert q.offer(e);
            notEmpty.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public E poll() {
        lock.lock();
        try {
            E x = q.poll();
            if (x != null)
                notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }

    public E peek() {
        lock.lock();
        try {
            return q.peek();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.lock();
        try {
            return q.contains(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        lock.lock();
        try {
            return q.toArray(ts);
        } finally {
            lock.unlock();
        }
    }

    public Comparator<? super E> comparator() {
        return q.comparator();
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return q.toString();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            q.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.lock();
        try {
            return q.remove(o);
        } finally {
            lock.unlock();
        }
    }

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        lock.lock();
        try {
            s.defaultWriteObject();
        } finally {
            lock.unlock();
        }
    }
}