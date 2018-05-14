package com.tech.analysis.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 */
@Repository
public class AuthorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getDisplayNameListByFullAddress(String enterpriseName){
        String sql = String.format("SELECT  display_name from AuthorTemp where full_address = '%s'",enterpriseName);
        List<String> displayNameList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("display_name");
            }
        });
        return displayNameList;
    }

    public void insertFromAuthorTemp(String aliasName){
        String sql = String.format("insert into Author (uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,full_address,addr_no,expertid) SELECT  A.uid,A.daisng_id,A.seq_no,A.reprint,A.display_name,A.full_name,A.wos_standard,A.last_name,A.email_addr,A.full_address,A.addr_no,B.id as expertid from AuthorTemp as A inner join expert as B on A.display_name = B.name where B.enterprisename = (select name from enterpriseInfo where id = (select companyid from CompanyAlias where aliasname = '%s')) and A.full_address like '%%%s%%' ",aliasName,aliasName);
        try{
            jdbcTemplate.update(sql);
        }catch(Exception e){

        }finally{

        }
    }
    public void deleteFormAuthorTemp(String aliasName){
        String sql = String.format("delete from AuthorTemp where full_address = '%s'",aliasName);
        jdbcTemplate.update(sql);
    }
}
