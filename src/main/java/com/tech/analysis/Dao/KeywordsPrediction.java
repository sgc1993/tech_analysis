package com.tech.analysis.Dao;

import com.tech.analysis.entity.KeywordEntity;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by XCY on 2018/4/25.
 */
@Repository
public class KeywordsPrediction {

//    private static HashMap<String, Long> keywordTimes = UtilRead.readKeywordTimes();
//    private static HashMap<String, Long> yearKeywordTimes = UtilRead.readYearKeywordTimes();
//    private static HashMap<String, KeywordEntity> keywords = UtilRead.readKeywords();

//    public void buildKeywordData(){
//
//    }

    /**
     * 执行构建数据，集训练模型，预测数据操作
     */
    public void predict(){
//        makeDataSet();
        System.out.println("start line predition ......");
        LearnAndPreWithLinerModel();
    }

    /**
     * 从neo4j中构建历史数据集，用于预测
     */
    public  void makeDataSet(){
        System.out.println("开始构建数据集。。。。。。");
        int lineCount = 0;
        HashMap<String, Long> keywordsTimes = makeKeywordsTimes();
        System.out.println(keywordsTimes.size());
//        HashMap<String, Long> yearKeywordsTimes = makeYearKeywordsTimes();
        HashMap<String, KeywordEntity> keywords = makeDictionary();
        System.out.println(keywords.size());
        ArrayList<HashMap<String,Long>> yearData = getYearData();
        int[] year = {2005,2008,2010,2012,2014,2016};
//        String filePath = "E:\\tech_analysis\\py\\model\\preDataSet.dat";
        String filePath = "/home/zhzy/Downloads/xcy/tech_analysis/py/model/preDataSet.dat";
        Writer write = null;
        try {
            write = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
            write.write("keyword,2005,2008,2010,2012,2014,2016\n");
            for (String keyword : keywords.keySet()){
                if (keywordsTimes.get(keyword) < 10)
                    continue;
                ++lineCount;
                String line = keyword;
                for (HashMap<String,Long> map : yearData){
                    if (map.get(keyword) == null)
                        line += ",0";
                    else line += ","+map.get(keyword);
                }
//                for (int i = 0; i < yearData.size(); ++i){
//                    if (yearData.get(i).get(keyword) == null)
//                        line += ",0";
//                    else line += ","+yearKeywordsTimes.get(keyword);
//                }
                System.out.println(line);
                write.write(line+'\n');
                write.flush();
            }
            write.close();
            System.out.println("制作训练集成功！");
            System.out.println("linCount: " + lineCount);
        }catch (Exception e){
            if (write != null)
                try {
                    write.close();
                    System.out.println("文件关闭成功");
                }catch (IOException eclose){
                    System.out.println("文件关闭失败");
                }
            System.out.println("制作训练集失败！");
            e.printStackTrace();
        }
    }


    /**
     * 预测下一年数据
     */

    public void LearnAndPreWithLinerModel(){
        try {
            System.out.println("Starting training linear model.....");
//            String[] parm = new String[] { "D:\\Python35\\python.exe",
//                    "E:\\tech_analysis\\py\\model\\Line_Prediction.py"};
            String[] parm = new String[] { "/usr/bin/python3",
                    "/home/zhzy/Downloads/xcy/tech_analysis/py/model/Line_Prediction.py"};

            Process pr = Runtime.getRuntime().exec(parm);
            pr.waitFor();
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            System.out.println("End python  LearnLinerModel训练预测完毕");
        }catch (Exception e){
            System.out.println("调用LearnLinerModel训练模型失败！");
        }
    }


    /**
     * 得到预测的keywords
     * 这一块还没写完，需要看前端需要返回什么格式，在继续写
     */
    public void getPredictionKeyword(){
        HashMap<String, Long> keywords = new HashMap<String, Long>();
        keywords = UtilRead.readPredictionKeyword();

    }

    /**
     * 得到每一年的数据
     * @return
     */
    public ArrayList<HashMap<String,Long>> getYearData(){
        HashMap<String, Long> yearKeywordsTimes = makeYearKeywordsTimes();
        ArrayList<HashMap<String,Long> > ans = new ArrayList<HashMap<String,Long> >();
        int[] years = {2005,2008,2010,2012,2014,2016};
//        int[] years = {2011};
        KeywordsDao keywordsDao = new KeywordsDao();
        for (int year : years){
            HashMap<String,Long>  data = creat(year,yearKeywordsTimes);
//            if (data != null)
//                ans.add(data);
            ans.add(data);
        }
        return ans;
    }

    /**
     * 得到每一年的keywords
     * @param year
     * @return
     */
    public HashMap<String,Long>  creat(int year,HashMap<String, Long> yearKeywordsTimes){
        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
        HashMap<String,Long> data = new HashMap<String,Long>();
        StatementResult result = connect.excute(
                "MATCH (n:yearNewKeyword) WHERE n.year = \""+year+"\" RETURN n.name AS name,n.year AS year",
                parameters( "", "" ));//获取结果集
//        StatementResult result = connect.excute(
//                "MATCH (n:yearNewKeyword) WHERE n.year = \""+year+"\" RETURN n.name AS name,n.year AS year," +
//                        "n.times AS times",
//                parameters( "", "" ));//获取结果集
        if (result == null)
            return null;

        int count = 0;
        int good = 0;
        int bad = 0;
        while ( result.hasNext() )
        {
            ++count;
            Record record = result.next();
            try {
                ArrayList<Integer> templist = new ArrayList<Integer>();
                String tempKeyword = record.get("name").asString();
                String string_name = tempKeyword.substring(0,tempKeyword.length()-4);
//                System.out.println(string_name);
//                long times = record.get("times").asInt();
//                data.put(string_name,times);
                data.put(string_name,yearKeywordsTimes.get(tempKeyword));
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

    /**
     * 制作字典
     */
    public HashMap<String, KeywordEntity> makeDictionary(){
        return UtilRead.readKeywords();
    }

    public HashMap<String, Long> makeYearKeywordsTimes(){
        return UtilRead.readYearKeywordTimes();
    }

    public HashMap<String, Long> makeKeywordsTimes(){
        return UtilRead.readKeywordTimes();
    }

}
