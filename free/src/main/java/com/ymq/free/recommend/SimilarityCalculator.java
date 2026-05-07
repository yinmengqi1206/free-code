package com.ymq.free.recommend;

/**
 * @Author yinmengqi
 * @Date 2024/6/21 12:33
 * @Version 1.0
 */

import java.util.Map;

public class SimilarityCalculator {
    public static double pearsonCorrelation(Map<Integer, Double> user1, Map<Integer, Double> user2) {
        double sumXY = 0, sumX = 0, sumY = 0, sumX2 = 0, sumY2 = 0;
        int n = 0;

        for (Integer movieId : user1.keySet()) {
            if (user2.containsKey(movieId)) {
                double rating1 = user1.get(movieId);
                double rating2 = user2.get(movieId);
                sumXY += rating1 * rating2;
                sumX += rating1;
                sumY += rating2;
                sumX2 += Math.pow(rating1, 2);
                sumY2 += Math.pow(rating2, 2);
                n++;
            }
        }

        if (n == 0) {
            return 0;
        }

        double denominator = Math.sqrt(sumX2 - Math.pow(sumX, 2) / n) * Math.sqrt(sumY2 - Math.pow(sumY, 2) / n);
        if (denominator == 0) {
            return 0;
        }

        return (sumXY - sumX * sumY / n) / denominator;
    }
}
