package com.tech.analysis.Dao;

import com.tech.analysis.entity.KeywordEntity;
import com.tech.analysis.entity.RelationshipEntity;

import java.io.*;
import java.util.HashMap;

/**
 * Created by XCY on 2018/4/18.
 */
public class UtilRead {

    /**
     * 读出模型
     * @return
     */
    public static HashMap<String, double[]> readModel(){
        FileInputStream freader;
        HashMap<String, double[]> wordMap  = new HashMap<String, double[]>();;
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\model.dat";
            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/model.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            wordMap = (HashMap<String, double[]>)objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入模型失败！");
        }
        System.out.println("载入模型成功！");
        return wordMap;
    }

    /**
     * 读出存储的keywords对象，用于加载新的数据时更新使用
     * @return
     */
    public static HashMap<String, KeywordEntity> readKeywords(){
        FileInputStream freader;
        HashMap<String, KeywordEntity> keywords = new HashMap<String, KeywordEntity>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\KeywordsObject.dat";
            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            keywords = (HashMap<String, KeywordEntity>)objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入keywords失败！");
        }
        System.out.println("载入keywords成功！");
        return keywords;
    }

    /**
     * 加载yearKeywords
     * @return
     */
    public static HashMap<String, KeywordEntity> readYearKeywords(){
        FileInputStream freader;
        HashMap<String, KeywordEntity> yearKeywords = new HashMap<String, KeywordEntity>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\YearKeywordsObject.dat";
            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            yearKeywords = (HashMap<String, KeywordEntity>)objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入yearKeywords失败！");
        }
        System.out.println("载入yearKeywords成功！");
        return yearKeywords;
    }

    /**
     * 加载关系relationships
     * @return
     */
    public static HashMap<String, RelationshipEntity> readRelationships(){
        FileInputStream freader;
        HashMap<String, RelationshipEntity> relationships = new HashMap<String, RelationshipEntity>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\RelationshipsObject.dat";
            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/RelationshipsObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            relationships = (HashMap<String, RelationshipEntity>)objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入relationships失败！");
        }
        System.out.println("载入relationships成功！");
        return relationships;
    }

    /**
     * 加载yearRelationships
     * @return
     */
    public static HashMap<String, RelationshipEntity> readYearRelationships(){
        FileInputStream freader;
        HashMap<String, RelationshipEntity> yearRelationships = new HashMap<String, RelationshipEntity>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\YearRelationshipsObject.dat";
            String filePath = "E/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearRelationshipsObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            yearRelationships = (HashMap<String, RelationshipEntity>)objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入yearRelationships失败！");
        }
        System.out.println("载入yearRelationships成功！");
        return yearRelationships;
    }

    /**
     * 加载keywordTimes
     * @return
     */
    public static HashMap<String, Long> readKeywordTimes(){
        FileInputStream freader;
        HashMap<String, Long> keywordTimes = new HashMap<String, Long>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\KeywordsTimesObject.dat";
            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsTimesObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            keywordTimes = (HashMap<String, Long>)objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入keywordTimes失败！");
        }
        System.out.println("载入keywordTimes成功！");
        return keywordTimes;
    }

    /**
     * 加载yearKeywordTimes
     * @return
     */
    public static HashMap<String, Long> readYearKeywordTimes(){
        FileInputStream freader;
        HashMap<String, Long> yearKeywordTimes = new HashMap<String, Long>();
        try {
//            String filePath = "E:\\tech_analysis\\py\\model\\YearKeywordsTimesObject.dat";
            String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsTimesObject.dat";
            freader = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
//            HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
            yearKeywordTimes = (HashMap<String, Long>)objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("载入yearKeywordTimes失败！");
        }
        System.out.println("载入yearKeywordTimes成功！");
        return yearKeywordTimes;
    }

    public static HashMap<String, Long> readPredictionKeyword() {
//        String prefilePath = "E:\\tech_analysis\\py\\model\\linearModelPrediction.dat";
        String prefilePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/linearModelPrediction.dat";
        HashMap<String, Long> prediction = new HashMap<String, Long>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(prefilePath));
            String line = "";
            while (true) {
                line = bufferedReader.readLine();
                if (line == null || line.trim().equals("")) break;
                String[] temps = line.split(",");
                if (Long.parseLong(temps[1].trim()) != 0)
                    prediction.put(temps[0].trim(),Long.parseLong(temps[1].trim()));
            }
        }catch (Exception e){
            System.out.println("读取预测数据失败");
        }
        System.out.println("读取预测数据成功");
        return prediction;
    }
}
