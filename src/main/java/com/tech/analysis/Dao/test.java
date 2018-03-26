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
//        String string = "\"One Belt One Road\"";
//        System.out.println(string);
//        System.out.println(string.replace("\""," "));
    }
}
