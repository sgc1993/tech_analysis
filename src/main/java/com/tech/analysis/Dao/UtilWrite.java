package com.tech.analysis.Dao;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by XCY on 2018/4/18.
 */
public class UtilWrite {
    public static void WriteModel(HashMap<String, double[]> wordMap) {
//        String filePath = "D:/Entity/institutionEntity.dat";
//        String filePath = "/home/zhzy/Documents/data/institutionEntity.dat";
        String filePath = "E:\\tech_analysis\\py\\model\\model.dat";
        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(wordMap);
            objectOutputStream.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("模型写入失败！");
        }
        System.out.println("模型写入成功！");
    }
}
