package com.ymq.free.recommend;

/**
 * @Author yinmengqi
 * @Date 2024/6/21 12:33
 * @Version 1.0
 */

import java.util.*;

public class Recommender {
    public static List<Integer> recommend(Map<Integer, Map<Integer, Double>> data, int userId, int k) {
        Map<Integer, Double> targetUserRatings = data.get(userId);
        PriorityQueue<UserSimilarity> topKNeighbors = new PriorityQueue<>(Comparator.comparingDouble(UserSimilarity::getSimilarity));
        Map<Integer, Double> candidateMovies = new HashMap<>();

        for (Integer otherUserId : data.keySet()) {
            if (otherUserId == userId) {
                continue;
            }
            double similarity = SimilarityCalculator.pearsonCorrelation(targetUserRatings, data.get(otherUserId));
            if (topKNeighbors.size() < k) {
                topKNeighbors.add(new UserSimilarity(otherUserId, similarity));
            } else if (similarity > topKNeighbors.peek().getSimilarity()) {
                topKNeighbors.poll();
                topKNeighbors.add(new UserSimilarity(otherUserId, similarity));
            }
        }

        for (UserSimilarity userSimilarity : topKNeighbors) {
            Map<Integer, Double> otherUserRatings = data.get(userSimilarity.getUserId());
            for (Integer movieId : otherUserRatings.keySet()) {
                if (!targetUserRatings.containsKey(movieId)) {
                    candidateMovies.put(movieId, candidateMovies.getOrDefault(movieId, 0.0) + otherUserRatings.get(movieId));
                }
            }
        }

        List<Integer> recommendedMovies = new ArrayList<>(candidateMovies.keySet());
        recommendedMovies.sort((m1, m2) -> Double.compare(candidateMovies.get(m2), candidateMovies.get(m1)));
        return recommendedMovies;
    }

    private static class UserSimilarity {
        private final int userId;
        private final double similarity;

        public UserSimilarity(int userId, double similarity) {
            this.userId = userId;
            this.similarity = similarity;
        }

        public int getUserId() {
            return userId;
        }

        public double getSimilarity() {
            return similarity;
        }
    }
}
