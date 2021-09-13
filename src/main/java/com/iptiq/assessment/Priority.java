package com.iptiq.assessment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Priority {
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    private int value;
}