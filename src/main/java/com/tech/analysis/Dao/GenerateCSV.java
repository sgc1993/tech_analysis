package com.tech.analysis.Dao;

import com.tech.analysis.entity.KeywordEntity;
import com.tech.analysis.entity.RelationshipEntity;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by XCY on 2018/3/26.
 * 产生节点CSV和关系CSV
 * id,name,Alias
 * from_id,times,to_id
 */
//@Repository
public class GenerateCSV {
    public void dealCSV(){
        String filename = "paper.dat";
        GenerateCSV generateCSV = new GenerateCSV();
        generateCSV.generate(filename);
    }
    private long nodeId = 0l;
    private long relationshipId = 0l;
    public List<String> getKeyWords(String line){//获取关键字列表
        List<String> keywords = new ArrayList<String>();
        String[] strings = line.trim().split(",");
        if (strings.length > 1){
            for (int i = 1; i < strings.length; i++){
                if (strings[i].trim().length() < 2)
                    continue;
                keywords.add(strings[i].trim().replace("\""," "));
            }
        }
        return keywords;
    }
    public List<String> getYearKeyWords(String line){//获取关键字列表
        List<String> keywords = new ArrayList<String>();
        String[] strings = line.trim().split(",");
        if (strings.length > 2){
            for (int i = 2; i < strings.length; i++){
                if (strings[i].trim().length() < 2)
                    continue;
                keywords.add(strings[i].trim().replace("\""," ")+strings[1].trim().replace("\""," "));
            }
        }
        return keywords;
    }

    public void generate(String file) {
        try {
            //存储不带年份的keywordTimes
            HashMap<String, Long> keywordTimes = new HashMap<String, Long>();
            //存储不带年份的keywords
            HashMap<String, KeywordEntity> keywords = new HashMap<String, KeywordEntity>();
            HashMap<String, RelationshipEntity> relationships = new HashMap<String, RelationshipEntity>();
            //keywords和relationships的ID

            //存储带年份的yearKeywordTimes
            HashMap<String, Long> yearKeywordTimes = new HashMap<String, Long>();
            //存储带年份的yearKeywords
            HashMap<String, KeywordEntity> yearKeywords = new HashMap<String, KeywordEntity>();
            HashMap<String, RelationshipEntity> yearRelationships = new HashMap<String, RelationshipEntity>();
            //yearKeywords和yearRelationships的ID
//            long yeadNodeId = 0l;
//            long yearRelationshipId = 0l;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";
            while (true) {
                line = bufferedReader.readLine();
                if (line == null || line.trim().equals("")) break;

                //获取keyword实体
                List<String> getKeywords = getKeyWords(line);
                List<Long> keywordIdList = new ArrayList<Long>();
                addLineKeyWords(keywords,keywordTimes,getKeywords,keywordIdList);
                addLineRelationship(relationships,keywordIdList);

                List<String> getYearKeywords = getYearKeyWords(line);
                List<Long> yearKeywordIdList = new ArrayList<Long>();
                addLineKeyWords(yearKeywords,yearKeywordTimes,getYearKeywords,yearKeywordIdList);
                addLineRelationship(yearRelationships,yearKeywordIdList);
            }
            System.out.println("keywordTime size : " + keywordTimes.size());
            System.out.println("keywords size : " + keywords.size());
            System.out.println("relationships size : " + relationships.size());

            System.out.println("yearKeywordTimes size : " + yearKeywordTimes.size());
            System.out.println("yearKeywords size : " + yearKeywords.size());
            System.out.println("yearRelationships size : " + yearRelationships.size());
            //写入文件中
            boolean flagkeywords = writeKeywordsCSV(keywords,"E:\\tech_analysis","keywords");
            boolean flagrelationship = writeRelationshipCSV(relationships,"E:\\tech_analysis","relationship");
            boolean flagyearKeywords = writeKeywordsCSV(yearKeywords,"E:\\tech_analysis","yearKeywords");
            boolean flagyearRelationship = writeRelationshipCSV(yearRelationships,"E:\\tech_analysis","yearRelationship");
            System.out.println(flagkeywords);
            System.out.println(flagrelationship);
            System.out.println(flagyearKeywords);
            System.out.println(flagyearRelationship);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addLineKeyWords(HashMap<String, KeywordEntity> keywords,
                                HashMap<String, Long> keywordTimes,
                                List<String> getKeywords,
                                List<Long> keywordIdList){
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
     * 每一行存入    id,name
     * @param keywords 需要写入的map
     * @param filePath 写入的路径
     * @param fileName 写入文件的名字
     * @return 是否写入成功
     */
    private boolean writeKeywordsCSV(HashMap<String, KeywordEntity> keywords, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;

        //文件总行数
        long count = 0;

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
            write.write("id,name\n");
            for (String str : keywords.keySet()){
//                if (keywords.get(str).getName().length() < 3)
//                    continue;
                String temp = keywords.get(str).getId()+","+keywords.get(str).getName();
                write.write(temp);
                write.write('\n');
                ++count;
            }
            write.flush();
            write.close();
        } catch (IOException e) {
            flag = false;
            System.out.println("keywords write error1 !");
            e.printStackTrace();
        }
        System.out.println(count);
        // 返回是否成功的标记
        return flag;
    }


    /**
     *
     * from_id,times,to_id
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
        long count = 0;

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
            write.write("from_id,times,to_id\n");
            for (String str : relationships.keySet()){
//                if (relationships.get(str))
//                    continue;
                String temp = relationships.get(str).getSource()+","+
                                relationships.get(str).getTimes()+","+
                                relationships.get(str).getTarget();
                write.write(temp);
                write.write('\n');
                ++count;
            }
            write.flush();
            write.close();
        }catch (IOException e){
            flag = false;
            System.out.println("relationship write error !");
            e.printStackTrace();
        }
        System.out.println(count);
        // 返回是否成功的标记
        return flag;
    }



}
