package com.meli.mutants.api.dtos.response;


import com.meli.mutants.business.domain.StatisticsBO;
import lombok.Value;

@Value
public class StatisticsDto {
    long countMutantDna;
    long countHumanDna;
    double ratio;

    public static StatisticsDto from(StatisticsBO statisticsBO) {
        return new StatisticsDto(statisticsBO.getMutantsCount(), statisticsBO.getHumansCount(), statisticsBO.getRatio());
    }
}
