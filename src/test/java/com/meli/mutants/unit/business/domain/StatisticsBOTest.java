package com.meli.mutants.unit.business.domain;

import com.meli.mutants.business.domain.StatisticsBO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StatisticsBOTest {

    @Test
    void getRatio_withSameCountOfHumanAndMutant_mustReturn1() {
        StatisticsBO statisticsBO = new StatisticsBO(100, 100);
        assertThat(statisticsBO.getRatio()).isEqualTo(1d);
    }

    @Test
    void getRatio_withDifferentCountOfHumanAndMutant_mustReturnDecimal() {
        StatisticsBO statisticsBO = new StatisticsBO(40, 100);
        assertThat(statisticsBO.getRatio()).isEqualTo(0.4d);
    }

    @Test
    void getRatio_withZeroMutants_mustReturnZero() {
        StatisticsBO statisticsBO = new StatisticsBO(0, 100);
        assertThat(statisticsBO.getRatio()).isEqualTo(0d);
    }

    @Test
    void getRatio_withZeroHumans_mustReturnInfinity() {
        StatisticsBO statisticsBO = new StatisticsBO(40, 0);
        assertThat(statisticsBO.getRatio()).isEqualTo(Double.POSITIVE_INFINITY);
    }
}
