package com.meli.mutants.business.domain;

import lombok.Getter;

@Getter
public class StatisticsBO {
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
