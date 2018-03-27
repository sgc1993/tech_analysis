package com.tech.analysis.Dao;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * 该类的主要功能是向neo4j中插入数据
 */
public class ImportNeo4j {
    public void importCSV(){
        String keywoeds ="LOAD CSV WITH HEADERS  FROM \"file:///keywords.csv\" AS line"+
                "MERGE (p:keyNode{id:line.id,name:line.name})";
        String relationship ="LOAD CSV WITH HEADERS FROM \"file:///relationship.csv\" AS line"+
                "match (from:keyNode{id:line.from_id}),(to:keyNode{id:line.to_id})"+
                "merge (from)-[r:similar{times:line.times}]->(to)";
        String yearKeywoeds ="LOAD CSV WITH HEADERS  FROM \"file:///yearKeywords.csv\" AS line"+
                "MERGE (p:yearKeyNode{id:line.id,name:line.name})";
        String yearrRelationship ="LOAD CSV WITH HEADERS FROM \"file:///yearRelationship.csv\" AS line"+
                "match (from:yearKeyNode{id:line.from_id}),(to:yearKeyNode{id:line.to_id})"+
                "merge (from)-[r:yearSimilar{times:line.times}]->(to)";
        boolean flagkeywords = neo4jExcute(keywoeds);
        boolean flagrelationship = neo4jExcute(relationship);
        boolean flagyearKeywords = neo4jExcute(yearKeywoeds);
        boolean flagyearRelationship = neo4jExcute(yearrRelationship);
        System.out.println(flagkeywords);
        System.out.println(flagrelationship);
        System.out.println(flagyearKeywords);
        System.out.println(flagyearRelationship);

    }
    public boolean neo4jExcute(String order){
        boolean flag = true;
        ConnectAndOperNeo4j connect = new ConnectAndOperNeo4j();
        try {
            connect.excute(order,parameters( "", "" ));
        }catch (Exception e){
            flag =false;
            System.out.println("import order error: " + order);
            e.printStackTrace();
        }
        return flag;
    }


}
