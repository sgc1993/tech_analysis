package com.tech.analysis.Dao;

import java.util.*;

import com.tech.analysis.entity.WordEntry;

/**
 * Created by XCY on 2018/4/18.
 */
public class WordModel {
    /**
     * 载入模型 HashMap<String, double[]>
     * @return HashMap<String, double[]>
     */
    HashMap<String, double[]> wordMap  = loadModel();
    private int topNSize = 6;

    public HashMap<String, double[]> loadModel(){
        HashMap<String, double[]> wordMap = UtilRead.readModel();
        return wordMap;
    }

    /**
     * 返回5个相似词
     * @param queryWord 查询词
     * @return 5个相近的词
     */
    public List<String> distance(String queryWord) {
        System.out.println("starting....");
//        System.out.println(queryWord);
        List<String> ans = new ArrayList<>();
        double[] center = wordMap.get(queryWord);
        System.out.println(center.length);
        if (center == null) {
            return null;
        }

        int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
        TreeSet<WordEntry> result = new TreeSet<WordEntry>();

        double min = Float.MIN_VALUE;
        for (Map.Entry<String, double[]> entry : wordMap.entrySet()) {
            double[] vector = entry.getValue();
            double dist = 0;
            for (int i = 0; i < vector.length; i++) {
                dist += center[i] * vector[i];
            }
            result.add(new WordEntry(entry.getKey(), dist));
                if (resultSize < result.size()) {
                    result.pollLast();
                }

//            if (dist > min) {
//                result.add(new WordEntry(entry.getKey(), dist));
//                if (resultSize < result.size()) {
//                    result.pollLast();
//                }
//                min = result.last().score;
//            }
        }
        result.pollFirst();

        for (WordEntry wordEntry : result){
            ans.add(wordEntry.name);
//            System.out.println(wordEntry.name);
        }
        return ans;
    }


}
