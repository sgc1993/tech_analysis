package com.tech.analysis.Dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

/**
 * Created by XCY on 2018/4/18.
 */
public class UtilRead {
    public static HashMap<String, double[]> readModel(){
        FileInputStream freader;
        HashMap<String, double[]> wordMap  = new HashMap<String, double[]>();;
        try {
            String filePath = "E:\\tech_analysis\\py\\model\\model.dat";
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
}
