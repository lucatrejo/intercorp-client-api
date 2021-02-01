package com.intercorp.client.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

class StatisticsHelperMethodsTest {

    @Test
    void average_empty() {
        Assertions.assertEquals(0.0, Helper.getAverage(Collections.EMPTY_LIST));
    }

    @Test
    void average_ok() {
        Assertions.assertEquals(30.0, Helper.getAverage(Arrays.asList(20, 30, 40)));
    }

    @Test
    void standard_deviation_ok() {
        Assertions.assertEquals(3.9377878103709665, Helper.getStandardDeviation(Arrays.asList(4,9,11,12,17,5,8,12,14)));
    }

}
