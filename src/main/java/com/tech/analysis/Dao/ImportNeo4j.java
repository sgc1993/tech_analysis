package com.tech.analysis.Dao;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * 该类的主要功能是向neo4j中插入数据
 */
public class ImportNeo4j {
    /**
     * neo4j导入数据
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


}
