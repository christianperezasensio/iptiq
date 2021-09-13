package com.iptiq.assessment;

import lombok.Getter;

@Getter
public class PriorityProcess extends Process implements Comparable<PriorityProcess> {

    private final Priority priority;

    public PriorityProcess(int id, Priority priority) {
        super(id);
        this.priority = priority;
    }

    @Override
    public int compareTo(PriorityProcess process) {
        return Integer.compare(this.getPriority().getValue(), process.getPriority().getValue());
    }
}
