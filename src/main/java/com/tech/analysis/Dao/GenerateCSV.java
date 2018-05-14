package com.tech.analysis.Dao;

import com.tech.analysis.entity.KeywordEntity;
import com.tech.analysis.entity.RelationshipEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by XCY on 2018/3/26.
 * 产生节点CSV和关系CSV
 * id,name,Alias
 * from_id,times,to_id
 */
@Repository
public class GenerateCSV {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private long nodeId = 0l;
    private long relationshipId = 0l;
    private long count = 0;
    private long yearCount = 0;

    /**
     * 产生导入数据所需的 csv 文件
     * 产生节点CSV和关系CSV
     * id,name,Alias
     * from_id,times,to_id
     */
    public void buildKeyAndRelationCSV(){
        try {
            getPaperAndPatentData();
//        String filename = "paper.dat";
            String filename = "paperAndPatent.dat";//用空格区分
            GenerateCSV generateCSV = new GenerateCSV();
            generateCSV.generate(filename);//得到关键字 和 relation CSV
            generateCSV.yearGenerate(filename);//得到关于年份的关键字 和 yearrelation CSV
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("从SQL数据库取出数据时发生错误！");
        }
    }

    /**
     * 从sql中拿出并存储标题、摘要的关键字
     * @throws Exception
     */

    public void getPaperAndPatentData() throws Exception{
        BufferedWriter out = new BufferedWriter(new FileWriter("paperAndPatent.dat"));
        try {
//            String[] sqls = {"select pubyear,keywords from paper ","select success_date,keywords from Patent "};
//            String[] sqls = {"select pubyear,keywords_cn from papertest ","select success_date,keywords_cn from Patent "};
            String[] sqls = {"select pubyear,keywords from papertest ","select success_date,keywords from Patent "};

            List<String> paperDataList = getSQLPaperback(sqls[0]);
            List<String> patentDataList = getSQLPatentback(sqls[1]);
            System.out.print("geted data from sql");
            System.out.println("paper datalist size: "+paperDataList.size());
            System.out.println("patent datalist size: "+patentDataList.size());
            System.out.println("start Writing data");
            int goodCount = 0;
            int goodCount1 = 0;
            int badCount = 0;
            int badCount1 = 0;
            if (paperDataList != null){
                for (String string : paperDataList){
                    if (string == null){
                        ++badCount;
                        continue;
                    }
                    ++goodCount;
                    out.write(string);
                    System.out.println("paper: "+string);
                    out.newLine();
                    out.flush();
                }
            }
            if (patentDataList != null){
                for (String string : patentDataList){
                    if (string == null){
                        ++badCount1;
                        continue;
                    }
                    ++goodCount1;
                    out.write(string);
                    System.out.println("patent: "+string);
                    out.newLine();
                    out.flush();
                }
            }
            System.out.println("goodCount: "+goodCount);
            System.out.println("goodCount1: "+goodCount1);
            System.out.println("badCount: "+badCount);
            System.out.println("badCount1: "+badCount1);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("写入keywords文件失败");
//            out.close();
        }finally {
            out.close();
        }

    }

    /**
     * 从sql数据库中拿出摘要和标题的关键字和年份
     * @param sql 命令
     * @return 查询出的信息
     */

    public List<String> getSQLPaperback(String sql){
        System.out.println("start get paper data from sql");
        List<String> dataList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String keywords =  rs.getString("keywords");
                String pubyear =  rs.getString("pubyear");
                if (pubyear == null)
                    return null;
//                System.out.println("paper: "+pubyear.trim()+" "+keywords);
                return pubyear.trim()+" "+keywords;
//                return null;
            }
        });
        System.out.println("paper datalist size: "+dataList.size());
        return dataList;
    }

    /**
     * 从sql数据库中拿出专利的关键字和年份
     * @param sql 命令
     * @return 查询出的信息
     */
    public List<String> getSQLPatentback(String sql){
        System.out.println("start get patent data from sql");
        List<String> dataList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String keywords =  rs.getString("keywords");
                String pubyear =  rs.getString("success_date");
                if (pubyear == null)
                    return null;
//                System.out.println("patent: "+pubyear.trim().substring(0,4)+" "+keywords);
                return pubyear.trim().substring(0,4)+" "+keywords;
//                return null;
            }
        });
        System.out.println("patent datalist size: "+dataList.size());
        return dataList;
    }

    /**
     * 返回关键字列表
     * @param line 读入的一行数据
     * @return
     */
//    public List<String> getKeyWords(String line,List<String> keywords){//获取关键字列表
    public void getKeyWords(String line,List<String> keywords){//获取关键字列表
//        List<String> keywords = new ArrayList<String>();
        String[] strings = line.trim().split(" "); //paperAndPatent.dat用空格区分
        if (strings.length > 2){
            for (int i = 1; i < strings.length; i++){
                if (strings[i].trim().length() < 2){
                    continue;
                }
                keywords.add(strings[i].trim().replace("\""," "));
            }
        }else {
            System.out.println("error line: " + line + " " + strings.length);
//            return null;
            return;
        }
//        return keywords;
    }

    /**
     * 返回带年份的关键字列表
     * @param line 读入数据
     * @return
     */
//    public List<String> getYearKeyWords(String line,List<String> keywords){//获取关键字列表
    public void getYearKeyWords(String line,List<String> keywords){//获取关键字列表
//        List<String> keywords = new ArrayList<String>();
        String[] strings = line.trim().split(" ");//paperAndPatent.dat用空格区分
        if (strings.length<=2 || strings[0].trim().length() != 4){
            ++yearCount;
            System.out.println("error data: " + line +" "+ strings.length);
            return;
        }
        if (strings.length > 2){
            for (int i = 1; i < strings.length; i++){
                if (strings[i].trim().length() < 2)
                    continue;
                keywords.add(strings[i].trim().replace("\""," ")+strings[0].trim().replace("\""," "));
            }
        }
//        return keywords;
    }

    /**
     * 给定文件名，将该文件写成CSV文件供写入neo4j使用,并把各个对象写下来，用于更新数据
     * @param file 文件名
     */
    public void generate(String file) {
        try {
            //存储不带年份的keywordTimes
            HashMap<String, Long> keywordTimes = new HashMap<String, Long>();
            //存储不带年份的keywords
            HashMap<String, KeywordEntity> keywords = new HashMap<String, KeywordEntity>();
            HashMap<String, RelationshipEntity> relationships = new HashMap<String, RelationshipEntity>();
            //keywords和relationships的ID

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";
            int lineCount = 0;
            //获取keyword实体
            List<String> getKeywords = new ArrayList<>();
            List<Long> keywordIdList = new ArrayList<Long>();
            while (true) {
                System.out.println("行数： "+(++lineCount));
                line = bufferedReader.readLine();
                if (line == null || line.trim().equals("")) break;
                if(getKeywords.size() > 0)
                    getKeywords.clear();
//                getKeywords = getKeyWords(line,getKeywords);//可以考虑将该对象传入函数，能减少大部分局部变量的创建
                getKeyWords(line,getKeywords);//可以考虑将该对象传入函数，能减少大部分局部变量的创建
//                if (getKeywords != null)
//                    System.out.println("keyword:   "+getKeywords);
                if (keywordIdList.size() > 0)
                    keywordIdList.clear();
                addLineKeyWords(keywords,keywordTimes,getKeywords,keywordIdList);
                addLineRelationship(relationships,keywordIdList);

//                getYearKeywords = getYearKeyWords(line,getYearKeywords);
//                if (getYearKeywords != null)
//                    System.out.println("getYearKeywords:   "+getYearKeywords);
            }
            System.out.println("keywordTime size : " + keywordTimes.size());
            System.out.println("keywords size : " + keywords.size());
            System.out.println("relationships size : " + relationships.size());

            //写入文件中
            boolean flagkeywords = writeKeywordsCSV(keywords,"/home/zhzy/Downloads/xcy/tech_analysis","keywords");
            boolean flagrelationship = writeRelationshipCSV(relationships,"/home/zhzy/Downloads/xcy/tech_analysis","relationship");

            UtilWrite.WriteKeywords(keywords);
            UtilWrite.WriteRelationships(relationships);
            UtilWrite.WriteKeywordsTimes(keywordTimes);

            System.out.println(flagkeywords);
            System.out.println(flagrelationship);
            System.out.println("errorline: "+count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void yearGenerate(String file) {
        try {
            //存储带年份的yearKeywordTimes
            HashMap<String, Long> yearKeywordTimes = new HashMap<String, Long>();
            //存储带年份的yearKeywords
            HashMap<String, KeywordEntity> yearKeywords = new HashMap<String, KeywordEntity>();
            HashMap<String, RelationshipEntity> yearRelationships = new HashMap<String, RelationshipEntity>();
            //yearKeywords和yearRelationships的ID
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";
            int lineCount = 0;
            //获取keyword实体
            List<String> getYearKeywords = new ArrayList<>();
            List<Long> yearKeywordIdList = new ArrayList<Long>();
            while (true) {
                System.out.println("行数： "+(++lineCount));
                line = bufferedReader.readLine();
                if (line == null || line.trim().equals("")) break;

                if (getYearKeywords.size() > 0)
                    getYearKeywords.clear();
//                getYearKeywords = getYearKeyWords(line,getYearKeywords);
                getYearKeyWords(line,getYearKeywords);
//                if (getYearKeywords != null)
//                    System.out.println("getYearKeywords:   "+getYearKeywords);
                yearKeywordIdList.clear();
                addLineKeyWords(yearKeywords,yearKeywordTimes,getYearKeywords,yearKeywordIdList);
                addLineRelationship(yearRelationships,yearKeywordIdList);
            }

            System.out.println("yearKeywordTimes size : " + yearKeywordTimes.size());
            System.out.println("yearKeywords size : " + yearKeywords.size());
            System.out.println("yearRelationships size : " + yearRelationships.size());
            //写入文件中
            boolean flagyearKeywords = writeYearKeywordsCSV(yearKeywords,"/home/zhzy/Downloads/xcy/tech_analysis","yearKeywords");
            boolean flagyearRelationship = writeYearRelationshipCSV(yearRelationships,"/home/zhzy/Downloads/xcy/tech_analysis","yearRelationship");


            UtilWrite.WriteYearKeywords(yearKeywords);
            UtilWrite.WriteYearRelationships(yearRelationships);
            UtilWrite.WriteYearKeywordsTimes(yearKeywordTimes);
            System.out.println("errorline: "+yearCount);
            System.out.println(flagyearKeywords);
            System.out.println(flagyearRelationship);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将改行关键字加入keywords
     * @param keywords
     * @param keywordTimes
     * @param getKeywords
     * @param keywordIdList
     */
    public void addLineKeyWords(HashMap<String, KeywordEntity> keywords,
                                HashMap<String, Long> keywordTimes,
                                List<String> getKeywords,
                                List<Long> keywordIdList){
        if (getKeywords == null) return;
        for (String keyword : getKeywords) {
            if (!keywordTimes.containsKey(keyword)) {
                keywordTimes.put(keyword, 1l);
            } else {
                keywordTimes.put(keyword, keywordTimes.get(keyword) + 1);
            }
            Long keywordId;
            if (!keywords.containsKey(keyword)) {
                KeywordEntity keywordEntity =
                        new KeywordEntity(keyword, nodeId);
                keywordId = nodeId;
                keywords.put(keyword, keywordEntity);
                nodeId++;
            } else {
                keywordId = keywords.get(keyword).getId();
            }
            keywordIdList.add(keywordId);
        }
    }

    /**
     * 将该行的共现关系加入relationships
     * @param relationships
     * @param keywordIdList
     */
    public void addLineRelationship(HashMap<String, RelationshipEntity> relationships,
                                    List<Long> keywordIdList){
        for (int i = 0; i < keywordIdList.size(); i++) {
            Long keywordId1 = keywordIdList.get(i);
            for (int j = i + 1; j < keywordIdList.size(); j++) {
                Long keywordId2 = keywordIdList.get(j);
                String keywordRelationshipHashKey1 = keywordId1.toString() + "_" + keywordId2.toString();
                String keywordRelationshipHashKey2 = keywordId2.toString() + "_" + keywordId1.toString();
                if (!relationships.containsKey(keywordRelationshipHashKey1) && !relationships.containsKey(keywordRelationshipHashKey2)) {
                    RelationshipEntity relationshipEntity =
                            new RelationshipEntity(keywordId1, keywordId2, 1l, "similar");
                    relationships.put(keywordRelationshipHashKey1, relationshipEntity);
                    relationshipId++;
                } else if (relationships.containsKey(keywordRelationshipHashKey1)) {
                    relationships.get(keywordRelationshipHashKey1).setTimes(relationships.get(keywordRelationshipHashKey1).getTimes() + 1l);
                } else if (relationships.containsKey(keywordRelationshipHashKey2)) {
                    relationships.get(keywordRelationshipHashKey2).setTimes(relationships.get(keywordRelationshipHashKey2).getTimes() + 1l);
                }
            }
        }
    }

    /**
     * 将keywords写入CSV
     * 每一行存入    label,id,name
     * @param keywords 需要写入的map
     * @param filePath 写入的路径
     * @param fileName 写入文件的名字
     * @return 是否写入成功
     */
    private boolean writeKeywordsCSV(HashMap<String, KeywordEntity> keywords, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countline = 0;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".csv";

        // 生成csv格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();


            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
//            Writer write = new OutputStreamWriter(new FileOutputStream(file), "GBK");
            write.write("l:label\tid:string:KeywordId\tname\n");
            for (String str : keywords.keySet()){
//                if (keywords.get(str).getName().length() < 3)
//                    continue;
                String temp = "newKeyword\t"+keywords.get(str).getId()+"\t"+keywords.get(str).getName();
                write.write(temp);
                write.write('\n');
                ++countline;
            }
            write.flush();
            write.close();
            System.out.println("关键字CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("keywords write error1 !");
            e.printStackTrace();
        }
        System.out.println(countline);
        // 返回是否成功的标记
        return flag;
    }

    /**
     * 将keywords写入CSV
     * 每一行存入    label,id,name,year
     * @param yearkeywords 需要写入的map
     * @param filePath 写入的路径
     * @param fileName 写入文件的名字
     * @return 是否写入成功
     */
    private boolean writeYearKeywordsCSV(HashMap<String, KeywordEntity> yearkeywords, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countyear = 0;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".csv";

        // 生成csv格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();


            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
//            Writer write = new OutputStreamWriter(new FileOutputStream(file), "GBK");
            write.write("l:label\tid:string:yearKeywordId\tname\tyear\n");
            for (String str : yearkeywords.keySet()){
                KeywordEntity key = yearkeywords.get(str);
                String year = key.getName().substring(key.getName().length()-4,
                        key.getName().length());
//                System.out.println("year: "+year+ " "+key.getName());
                String temp = "yearNewKeyword\t"+key.getId()+"\t"+key.getName()+"\t"+year;
                write.write(temp);
                write.write('\n');
                ++countyear;
            }
            write.flush();
            write.close();
            System.out.println("包含年份关键字CSV写入成功！");
        } catch (IOException e) {
            flag = false;
            System.out.println("keywords write error1 !");
            e.printStackTrace();
        }
        System.out.println(countyear);
        // 返回是否成功的标记
        return flag;
    }


    /**
     *将关系写入CSV
     * id:string:KeywordId	id:string:KeywordId	type	times
     * @param relationships
     * @param filePath
     * @param fileName
     * @return
     */
    private boolean writeRelationshipCSV(HashMap<String, RelationshipEntity> relationships,
                                         String filePath, String fileName){
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countre = 0;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".csv";

        // 生成csv格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "GBK");
            write.write("id:string:KeywordId\tid:string:KeywordId\ttype\ttimes\n");
            for (String str : relationships.keySet()){
//                if (relationships.get(str))
//                    continue;
                String temp = relationships.get(str).getSource()+"\t"+
                                relationships.get(str).getTarget()+"\t"+"similar\t"+
                                relationships.get(str).getTimes();
                write.write(temp);
                write.write('\n');
                ++countre;
            }
            write.flush();
            write.close();
            System.out.println("关系CSV写入成功！");
        }catch (IOException e){
            flag = false;
            System.out.println("relationship write error !");
            e.printStackTrace();
        }
        System.out.println(countre);
        // 返回是否成功的标记
        return flag;
    }

    /**
     *将关系写入CSV
     * id:string:yearKeywordId	id:string:yearKeywordId	type	times
     * @param relationships
     * @param filePath
     * @param fileName
     * @return
     */
    private boolean writeYearRelationshipCSV(HashMap<String, RelationshipEntity> relationships,
                                         String filePath, String fileName){
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long countreyear = 0;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".csv";

        // 生成csv格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "GBK");
            write.write("id:string:yearKeywordId\tid:string:yearKeywordId\ttype\ttimes\n");
            for (String str : relationships.keySet()){
                String temp = relationships.get(str).getSource()+"\t"+
                            relationships.get(str).getTarget()+"\t"+"yearSimilar\t"+
                            relationships.get(str).getTimes()+'\n';
                write.write(temp);
//                write.write('\n');
                ++countreyear;
            }
            write.flush();
            write.close();
            System.out.println("包含年份关系CSV写入成功！");
        }catch (IOException e){
            flag = false;
            System.out.println("relationship write error !");
            e.printStackTrace();
        }
        System.out.println(countreyear);
        // 返回是否成功的标记
        return flag;
    }



}
