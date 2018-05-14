package com.tech.analysis.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by XCY on 2018/4/18.
 * 该类主要用于创建模型
 */
@Repository
public class LoadWordAndVector {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据语料生成模型
     */
    public void buildModel(){
        try {
            getOriginalData();
            GetWordAndVexDoc();
            creatModel();
        }catch (IOException e){
            System.out.println("Ori文件写入错误！");
        }

    }

    /**
     * 从数据库中取出摘要、标题等构成OriginalData.dat  内容是摘要和标题
     */

    public void getOriginalData() throws IOException{
        BufferedWriter out = new BufferedWriter(new FileWriter("E:\\tech_analysis\\py\\model\\OriginalData.dat"));;
        try {
            String sql = "select abstract_text_cn,title_cn from Paper ";

            List<String> dataList = jdbcTemplate.query(sql, new RowMapper<String>(){
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    String abstract_text =  rs.getString("abstract_text_cn");
                    String title_text =  rs.getString("title_cn");
                    if (abstract_text == null && title_text == null)
                        return null;
                    System.out.println(abstract_text+title_text);

                    return title_text+abstract_text;
                }
            });
            int goodCount = 0;
            int badCount = 0;
            if (dataList != null){
                for (String string : dataList){
                    if (string == null){
                        ++badCount;
                        continue;
                    }
                    ++goodCount;
                    out.write(string);
                out.newLine();
                out.flush();
                }
            }
            System.out.println("goodCount: "+goodCount);
            System.out.println("badCount: "+badCount);
        }catch (Exception e){
            System.out.println("写入文件失败");
//            out.close();
        }finally {
            out.close();
        }

    }

    /**
     * 创建加载模型所需的Word2Vec文件
     *
     */
    public void GetWordAndVexDoc(){
        try {
            System.out.println("Starting.....");
            String[] parm = new String[] { "D:\\Python35\\python.exe",
                    "E:\\tech_analysis\\py\\model\\Word2VectorBasedGensim.py"};

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
            System.out.println("End python，word2vector训练完毕");
        }catch (Exception e){
            System.out.println("调用GetWordAndVexDoc训练模型失败！");
        }
    }
    /**
     * 加载词和向量的文件，构建成以词为键，向量为值的hashmap
     */
    public void creatModel(){
//        String filename = "E:\\tech_analysis\\py\\model\\Word2Vec.dat"; //词和向量存储的文件位置
        String filename = "Word2Vec.dat"; //词和向量存储的文件位置
        BufferedReader in;
        HashMap<String, double[]> wordMap = new HashMap<String, double[]>();
        int i = 0;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String line;
            while ((line = in.readLine()) != null){
                ++i;
//                if (i > 10)
//                    break;
                String[] split = line.split(",");
                double[] doubles = new double[200];
                for (int index = 1; index < split.length-1;++index)
                    doubles[index] = Double.parseDouble(split[index]);
                wordMap.put(split[0],doubles);
//                System.out.println();
            }
            System.out.println(i);
            System.out.println(wordMap.size());
            UtilWrite.WriteModel(wordMap);
        }catch (Exception e){
            System.out.println("读取词和向量文件失败！");
        }
    }



    public static void main(String[] args){
//        new LoadWordAndVector().getOriginalData();
//        new LoadWordAndVector().GetWordAndVexDoc();
        new LoadWordAndVector().creatModel();
//        System.out.println(Double.parseDouble("12.3"));
    }
}
