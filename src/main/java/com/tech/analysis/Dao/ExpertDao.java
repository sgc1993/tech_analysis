package com.tech.analysis.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18 0018.
 */
@Repository
public class ExpertDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getIdList(){
        String sql = "select id from Expert";
        List<String> idList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String id = rs.getString("id");
                return id;
            }
        });
        return idList;
    }
    public List<String> getKeywordByExpertId(String table,String expertId){
        String sql = "";
        if(table.equals("paper"))
            sql = String.format("select keywords from paper where uid in(select a.uid from Expert e  inner join Author a on e.id = a.expertid where e.id = %s)", expertId);
        else if(table.equals("patent"))
            sql = String.format("select keywords from patent where id in (select p.patentid from expert e inner join patent2expert p on e.id = p.expertid where e.id = %s)",expertId);
        else if(table.equals("project"))
            sql = String.format("select keywords from project where id in (select p.projectid from expert e inner join project2expert p on e.id = p.expertid where e.id = %s)",expertId);
        List<String> keywordsList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String keywords = rs.getString("keywords");
                return keywords;
            }
        });
        return keywordsList;
    }

    public void putKeywordByExpertId(String expertId,String keywords){
        jdbcTemplate.update(String.format("update Expert set keywords = '%s' where id = '%s'",keywords,expertId));
    }

    public List<String> insertByEnterpriseNameAndName(String enterpriseName,List<String> nameList){
        List<String> expertIdList = new ArrayList<>();
//            String sql = "insert into expert (name,enterprisename,isdeath) values";
//            for (String name : nameList) {
//                sql += String.format("('%s','%s',0),",name,enterpriseName);
//            }
//            sql = sql.substring(0,sql.length()-1);
//            jdbcTemplate.update(sql);
        for (String name : nameList) {
            String sql = String.format("insert into expert (name,enterprisename,isdeath) values ('%s','%s',0)",name,enterpriseName);

            try{
                jdbcTemplate.update(sql);
                expertIdList.add(jdbcTemplate.queryForObject(String.format("select id from expert where name = '%s' and enterprisename = '%s'",name,enterpriseName),String.class));
            }catch(Exception e){

            }finally {

            }
        }
        return expertIdList;
    }

    public String getExpertIdByNameAndEnterpriseName(String name, String enterpriseName){
        String sql = String.format("Select id from Expert where name = '%s' and enterpriseName = '%s'",name,enterpriseName);
        return (String) jdbcTemplate.queryForObject(sql,String.class);
    }
}
