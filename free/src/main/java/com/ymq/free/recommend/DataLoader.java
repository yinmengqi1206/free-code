package com.ymq.free.recommend;

/**
 * @Author yinmengqi
 * @Date 2024/6/21 12:33
 * @Version 1.0
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class DataLoader {
    public static Map<Integer, Map<Integer, Double>> loadData(String filePath) {
        Map<Integer, Map<Integer, Double>> data = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                int userId = Integer.parseInt(tokens[0].replace("\uFEFF", ""));
                int movieId = Integer.parseInt(tokens[1]);
                double rating = Double.parseDouble(tokens[2]);
                data.putIfAbsent(userId, new HashMap<>());
                data.get(userId).put(movieId, rating);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("数据大小" + data.size());
        return data;
    }
}
