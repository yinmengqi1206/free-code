package com.ymq.free.recommend;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author yinmengqi
 * @Date 2024/6/21 12:34
 * @Version 1.0
 */
public class Main {
    public static void main(String[] args) {
        String root = System.getProperty("user.dir");
        String fileName = "ratings.csv";
        String filePath = root + File.separator + "free/src/main/resources" + File.separator + fileName;
        Map<Integer, Map<Integer, Double>> data = DataLoader.loadData(filePath);
        List<Integer> recommendedMovies = Recommender.recommend(data, 22, 5);
        System.out.println("Recommended movies for user 1: " + recommendedMovies);
    }
}
