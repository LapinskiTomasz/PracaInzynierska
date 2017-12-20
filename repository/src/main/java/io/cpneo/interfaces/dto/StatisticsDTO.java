package io.cpneo.interfaces.dto;

import lombok.Data;

@Data
public class StatisticsDTO {

    int fiveStats;

    int fourStats;

    int threeStats;

    int twoStats;

    int oneStats;

    @Override
    public String toString() {
        return "StatisticsDTO{" +
                "fiveStats=" + fiveStats +
                ", fourStats=" + fourStats +
                ", threeStats=" + threeStats +
                ", twoStats=" + twoStats +
                ", oneStats=" + oneStats +
                '}';
    }
}
