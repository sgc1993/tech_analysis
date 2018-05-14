package com.tech.analysis.Dao;

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * 该类的主要功能是向neo4j中插入数据 importCSV适合1--100000数据量   batch_import适合大数据
 */
//@Repository
public class ImportNeo4j {
    /**
     * neo4j导入数据
     *
     */
    public void importCSV(){
        String keywoeds ="LOAD CSV WITH HEADERS  FROM \"file:///keywords.csv\" AS line"+
                " MERGE (p:keyNode{id:line.id,name:line.name})";
        String relationship ="LOAD CSV WITH HEADERS FROM \"file:///relationship.csv\" AS line"+
                " match (from:keyNode{id:line.from_id}),(to:keyNode{id:line.to_id})"+
                " merge (from)-[r:wordsSimilar{times:line.times}]->(to)";
        String yearKeywoeds ="LOAD CSV WITH HEADERS  FROM \"file:///yearKeywords.csv\" AS line"+
                " MERGE (p:yearKeyNode{id:line.id,name:line.name,year:line.year})";
        String yearrRelationship ="LOAD CSV WITH HEADERS FROM \"file:///yearRelationship.csv\" AS line"+
                " match (from:yearKeyNode{id:line.from_id}),(to:yearKeyNode{id:line.to_id})"+
                " merge (from)-[r:yearWordsSimilar{times:line.times}]->(to)";
//        System.out.println("执行keywoeds中。。。");
//        boolean flagkeywords = neo4jExcute(keywoeds);
//        System.out.println("keywoeds: "+flagkeywords);
        System.out.println("执行relationship中。。。");
        boolean flagrelationship = neo4jExcute(relationship);
        System.out.println("relationship:  "+flagrelationship);
        System.out.println("执行yearKeywoeds中。。。");
        boolean flagyearKeywords = neo4jExcute(yearKeywoeds);
        System.out.println("yearKeywoeds:  "+flagyearKeywords);
        System.out.println("执行yearrRelationship中。。。");
        boolean flagyearRelationship = neo4jExcute(yearrRelationship);
        System.out.println("yearrRelationship:  "+flagyearRelationship);

    }

    /**
     * 连接数据库执行操作
     * @param order 执行命令
     * @return
     */
    public boolean neo4jExcute(String order){
        boolean flag = true;
        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
        try {
            connect.excute(order,parameters( "", "" ));
        }catch (Exception e){
            flag =false;
            System.out.println("import order error: " + order);
            e.printStackTrace();
        }finally {
            connect.closeConnect();
        }
        return flag;
    }


    /**
     * 开启neo4j数据库
     */
    public void startNeo4j(){
        String[] parm = new String[] { "/usr/neo4j3.1.0/bin/neo4j", "start"};
        runConsoleOrder(parm);
    }


    /**
     * 关闭neo4j数据库
     */
    public void stopNeo4j(){
        String[] parm = new String[] { "/usr/neo4j3.1.0/bin/neo4j", "stop"};
        runConsoleOrder(parm);
    }


    /**
     * 批量导入neo4j数据库
     */
    public void batch_import(){
        stopNeo4j();
        String[] deleteGraph_db = new String[] { "rm", "-rf",
                "/usr/neo4j3.1.0/data/databases/graph.db"};
        String[] unzipGraph_db = new String[] { "unzip",
                "/usr/neo4j3.1.0/data/databases/graph.db.zip"};
        String[] keyImport = new String[] { "/home/zhzy/Downloads/xcy/batch-import-tool/import.sh",
                "file/keywords.csv", "file/relationship.csv"};
        String[] yearKeyImport = new String[] { "/home/zhzy/Downloads/xcy/batch-import-tool/import.sh",
                "file/yearKeywords.csv", "file/yearRelationship.csv"};


        runConsoleOrder(unzipGraph_db);
        runConsoleOrder(deleteGraph_db);
        runConsoleOrder(keyImport);
        runConsoleOrder(yearKeyImport);
        startNeo4j();
    }

    /**
     * 执行命令行参数
     * @param parm 参数列表
     */
    public void runConsoleOrder(String[] parm){
        try {
            System.out.println("Starting .....");
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
            System.out.println("End .....");
        }catch (Exception e){
            System.out.println("执行命令失败！");
        }
    }
}
