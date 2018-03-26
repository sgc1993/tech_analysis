package com.tech.analysis.Dao;

import java.util.ArrayList;
import java.util.HashMap;

//import org
public class test {
    public static void main(String[] args){
        System.out.println("test");
        KeywordsDao keywordsDao = new KeywordsDao();
        HashMap<String,ArrayList<Integer>> data = keywordsDao.getData("");
        String str =keywordsDao.formatJsonString(keywordsDao.getCommunity(data));
        System.out.println(str);
    }
}
