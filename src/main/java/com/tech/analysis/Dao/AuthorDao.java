package com.tech.analysis.Dao;

import com.tech.analysis.entity.Expert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 */
@Repository
public class AuthorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getDisplayNameListByFullAddress(String enterpriseName){
        String sql = String.format("SELECT  display_name from AuthorTemp where full_address like '%s%%'",enterpriseName);
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

    /**
     * @return 对authorTemp 和  Address 联合查找，找出机构能被对上的作者的信息
     */
    public List<Expert> getAuthorForNewExpertList(){
        String sql = "SELECT DISTINCT display_name,companyid,organization\n" +
                "  FROM AuthorTemp a inner join Address b on a.uid = b.uid and a.full_address = b.full_address";
        List<Expert> list = new ArrayList<>();
        list = jdbcTemplate.query(sql, new RowMapper<Expert>() {
            @Override
            public Expert mapRow(ResultSet rs, int i) throws SQLException {
                Expert e = new Expert();
                e.setName(rs.getString("display_name"));
                e.setEnterpriseName(rs.getString("organization"));
                e.setEnterpriseId(rs.getString("companyid"));
                return e;
            }
        });
        return list;
    }

    /**
     * 将temp中能对出的数据插入Author中，并将Temp中数据删除
     */
    public void updateAuthorAndDeleteInTemp(){
        String sql = "insert into Author (uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,full_address,addr_no,expertid)  (select distinct d.uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,d.full_address,d.addr_no,c.id as expertid from Expert c inner join (SELECT a.uid,daisng_id,seq_no,reprint,display_name,full_name,wos_standard,last_name,email_addr,a.full_address,a.addr_no,organization\n" +
                "  FROM AuthorTemp a inner join Address b on a.uid = b.uid and a.full_address = b.full_address) d on c.name = d.display_name and c.enterprisename = d.organization)\n" +
                "\n" +
                " delete from AuthorTemp where id in (select a.id from AuthorTemp a inner join Author b on a.uid = b.uid and a.display_name = b.display_name and a.full_address = b.full_address)";
        jdbcTemplate.update(sql);
    }

}
