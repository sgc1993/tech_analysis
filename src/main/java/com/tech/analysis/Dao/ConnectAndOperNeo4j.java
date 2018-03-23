package com.tech.analysis.Dao;
import org.neo4j.driver.v1.*;

/**
 * 主要提供neo4j的连接
 */
public class ConnectAndOperNeo4j {
    private org.neo4j.driver.v1.Driver driver = GraphDatabase.driver( "bolt://10.168.103.78:7687",
            AuthTokens.basic( "neo4j", "123456" ));//配置驱动
    private Session session = driver.session();
//    /**
//     * @return connect
//     */
//    public Session getSession(){
//        return session;
//    }

    /**
     * close connection
     */
    public void closeConnect(){
        session.close();
        driver.close();
    }

    public StatementResult excute(String order, Value value){
        StatementResult result = null;
        try {
            result = session.run(order,value);
        }catch (Exception e){
            System.out.println("order maybe error. please check!!! " + e);
        }
        return result;
    }
}