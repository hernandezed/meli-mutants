package com.meli.mutants.business.domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class StatisticsBO implements Serializable {
    private static final long serialVersionUID = 1L;

    long mutantsCount;
    long humansCount;

    public StatisticsBO(long mutantsCount, long humansCount) {
        this.mutantsCount = mutantsCount;
        this.humansCount = humansCount;
    }


    public double getRatio() {
        return (double) mutantsCount / humansCount;
    }
}
