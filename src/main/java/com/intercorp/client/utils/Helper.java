package com.intercorp.client.utils;

import java.util.Collection;

public class Helper {

    public static double getAverage(Collection<Integer> numbers) {
        return  numbers
                .stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public static double getStandardDeviation(Collection<Integer> values) {
        double average = getAverage(values);
        double standardDeviation = 0.0;
        for (double num : values) {
            standardDeviation += Math.pow(num - average, 2);
        }

        return Math.sqrt(standardDeviation / values.size());
    }
}
