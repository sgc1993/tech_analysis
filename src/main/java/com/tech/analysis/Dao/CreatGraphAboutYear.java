package com.tech.analysis.Dao;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * 按年份生成图
 * Created by XCY on 2018/3/27.
 */
@Repository
public class CreatGraphAboutYear {
    /**
     * 返回给定年份的社区列表
     * @return
     */
    public List<String> creatAll(){
        List<String> ans = new ArrayList<>();
        int[] years = {2005,2008,2010,2012,2014,2016};
//        int[] years = {2011};
        KeywordsDao keywordsDao = new KeywordsDao();
        for (int year : years){
            HashMap<String,ArrayList<Integer>> data = creat(year);
            String tempCommunity =keywordsDao.formatJsonString(keywordsDao.getCommunity(data));
            ans.add(tempCommunity);
        }
//        HashMap<String,ArrayList<Integer>> data = keywordsDao.getData("");
//        String str =keywordsDao.formatJsonString(keywordsDao.getCommunity(data));
        return ans;
    }

    /**
     * 创造出给定年份的图
     * @param year
     * @return
     * MATCH (n:yearNewKeyword) WHERE n.year = "2011" return  n.name,n.partitionKey,n.partitionKey1
     */
    public HashMap<String,ArrayList<Integer>> creat(int year){
        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
        HashMap<String,ArrayList<Integer>> data = new HashMap<String,ArrayList<Integer>>();
        StatementResult result = connect.excute(
                "MATCH (n:yearNewKeyword) WHERE n.year = \""+year+"\" RETURN n.name AS name,n.year AS year," +
                        "n.partitionKey AS partitionKey,n.partitionKey1 AS partitionKey1",
                parameters( "", "" ));//获取结果集
//        StatementResult result = connect.excute(
//                "MATCH (n:yearNewKeyword) WHERE n.year = \""+ year +" \" RETURN n.name AS name,n.year AS year," +
//                        "n.partitionKey AS partitionKey,n.partitionKey1 AS partitionKey1",
//                parameters( "", "" ));//获取结果集
//        StatementResult result = connect.excute(
//                "MATCH (n:yearKeyNode) WHERE n.year = "+ year +" RETURN n.name AS name,n.year AS year," +
//                        "n.Community AS Community,n.Community1 AS Community1",
//                parameters( "", "" ));//获取结果集

        int count = 0;
        int good = 0;
        int bad = 0;
        while ( result.hasNext() )
        {
            ++count;
            Record record = result.next();
            try {
                ArrayList<Integer> templist = new ArrayList<Integer>();
//                System.out.println(record.get("partitionKey").asInt());
//                System.out.println(record.get("partitionKey1").asInt());
//                System.out.println(record.get( "name" ).asString());
                templist.add(record.get("partitionKey").asInt());
                templist.add(record.get("partitionKey1").asInt());
                data.put(record.get( "name" ).asString(),templist);
                ++good;
            }catch (Exception e){
                ++bad;
                continue;
            }
        }
        connect.closeConnect();
        System.out.println("year: "+ year + "总数： "+count);
        System.out.println("year: "+ year + "bad总数： "+bad);
        System.out.println("year: "+ year + "good总数： "+good);
        return data;
    }
}
