package com.tech.analysis.Dao;

import com.tech.analysis.entity.KeywordEntity;
import com.tech.analysis.entity.RelationshipEntity;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by XCY on 2018/4/18.
 */
public class UtilWrite {
    /**
     * 将HashMap<String, double[]> wordMap 写入文件
     * @param wordMap
     */
    public static void WriteModel(HashMap<String, double[]> wordMap) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\model.dat";
        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/model.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(wordMap);
            objectOutputStream.close();
            System.out.println("模型写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("模型写入失败！");
        }
//        System.out.println("模型写入成功！");
    }

    /**
     * 将以前构建的关键字map对象存储下来，用于追加数据是构建新的keywords CSV
     * @param keywords
     */
    public static void WriteKeywords(HashMap<String, KeywordEntity> keywords) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\KeywordsObject.dat";
        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(keywords);
            objectOutputStream.close();
            System.out.println("关键字对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("关键字对象写入失败！");
        }
//        System.out.println("关键字写入成功！");
    }

    /**
     * 将以前构建的包含年份的关键字map对象存储下来，用于追加数据是构建新的yearKeywords CSV
     * @param yearKeywords
     */
    public static void WriteYearKeywords(HashMap<String, KeywordEntity> yearKeywords) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\YearKeywordsObject.dat";
        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(yearKeywords);
            objectOutputStream.close();
            System.out.println("包含年份的关键字对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("包含年份的关键字对象写入失败！");
        }
//        System.out.println("包含年份的关键字对象写入成功！");
    }

    /**
     * 将以前构建的关系的对象存起来
     * @param relationships
     */
    public static void WriteRelationships(HashMap<String, RelationshipEntity> relationships) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\RelationshipsObject.dat";
        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/RelationshipsObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(relationships);
            objectOutputStream.close();
            System.out.println("关系对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("关系对象写入失败！");
        }
//        System.out.println("关系写入成功！");
    }

    /**
     * 将以前构建的包含年份的关系的对象存储起来
     * @param yearRelationships
     */
    public static void WriteYearRelationships(HashMap<String, RelationshipEntity> yearRelationships) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\YearRelationshipsObject.dat";
        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearRelationshipsObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(yearRelationships);
            objectOutputStream.close();
            System.out.println("包含年份的关系对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("包含年份的关系对象写入失败！");
        }
//        System.out.println("包含年份的关系写入成功！");
    }

    /**
     * 将构建的关键字次数的对象存储起来
     * @param keywordTimes
     */
    public static void WriteKeywordsTimes(HashMap<String, Long> keywordTimes) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\KeywordsTimesObject.dat";
        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/KeywordsTimesObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(keywordTimes);
            objectOutputStream.close();
            System.out.println("关键字的次数对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("关键字的次数对象写入失败！");
        }
//        System.out.println("关键字的次数写入成功！");
    }

    /**
     *
     * 将构建的包含年份的关键字次数的对象存储起来
     * @param yearKeywordTimes
     */
    public static void WriteYearKeywordsTimes(HashMap<String, Long> yearKeywordTimes) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
//        String filePath = "E:\\tech_analysis\\py\\model\\YearKeywordsTimesObject.dat";
        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/YearKeywordsTimesObject.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(yearKeywordTimes);
            objectOutputStream.close();
            System.out.println("包含年份的关键字次数对象写入成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("包含年份的关键字次数对象写入失败！");
        }
//        System.out.println("包含年份的关键字次数写入成功！");
    }
}
