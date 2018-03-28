package com.tech.analysis.Dao;

import java.util.ArrayList;
import java.util.HashMap;

//import org
public class test {
    public static void main(String[] args){
//        System.out.println("test");
//        KeywordsDao keywordsDao = new KeywordsDao();
//        HashMap<String,ArrayList<Integer>> data = keywordsDao.getData("");
//        String str =keywordsDao.formatJsonString(keywordsDao.getCommunity(data));
//        System.out.println(str);

        System.out.println("test generateCSV");
        String filename = "paper.dat";
        GenerateCSV generateCSV = new GenerateCSV();
        generateCSV.dealCSV();

//        new ImportNeo4j().importCSV();

//        String string = "\"One Belt One Road\"";
//        System.out.println(string);
//        System.out.println(string.replace("\""," "));

//        String temp = "石英制品拉丝机控制系统!2004!光纤!拉丝机!石墨炉!过程控制总线!控制网络!1";
//        String[] str = temp.split("!");
//        System.out.println(str.length);
//        for (String string:str) {
//            System.out.print(string + " ");
//        }
    }
}
