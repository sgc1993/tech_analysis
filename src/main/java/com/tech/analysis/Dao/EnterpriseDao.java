package com.tech.analysis.Dao;

import com.tech.analysis.entity.Enterprise;
import org.neo4j.cypher.internal.compiler.v3_1.commands.expressions.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21 0021.
 */
@Repository
public class EnterpriseDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Enterprise getEnterpriseById(Integer id) {

        String sql = "SELECT name FROM EnterpriseInfo WHERE id = ?";
        RowMapper<Enterprise> rowMapper = new BeanPropertyRowMapper<>(Enterprise.class);
        Enterprise enterprise = jdbcTemplate.queryForObject(sql, rowMapper, id);

        return enterprise;
    }

    public List<Enterprise> getAllEnterpriseList(){
        String sql = "SELECT name,chuziqiye,zhuceziben,hangye,zhuceriqi,code,type,zuzhixingshi,level,zhucedi,id FROM EnterpriseInfo";
        List<Enterprise> list =  (List<Enterprise>) jdbcTemplate.query(sql, new RowMapper<Enterprise>(){

            @Override
            public Enterprise mapRow(ResultSet rs, int rowNum) throws SQLException {
                Enterprise enterprise = new Enterprise();
                enterprise.setName(rs.getString("name"));
                enterprise.setChuziqiye(rs.getString("chuziqiye"));
                enterprise.setZhuceziben(rs.getString("zhuceziben"));
                enterprise.setHangye(rs.getString("hangye"));
                enterprise.setZhuceriqi(rs.getString("zhuceriqi"));
                enterprise.setCode(rs.getString("code"));
                enterprise.setType(rs.getString("type"));
                enterprise.setZuzhixingshi(rs.getString("zuzhixingshi"));
                enterprise.setLevel(rs.getString("level"));
                enterprise.setZhucedi(rs.getString("zhucedi"));
                enterprise.setId(rs.getString("id"));

                return enterprise;
            }

        });
        return  list;
    }

    public List<String> getAllEnterpriseNameList(){
        List<String> nameList = new ArrayList<>();
        String sql = "select name from EnterpriseInfo";
        //RowMapper<String> rowMapper = new BeanPropertyRowMapper<>(String.class);
        nameList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("name");
            }
        });
        return nameList;
    }

    /**
     * @param enterprise 根据传来的企业实体信息，去EnterpriseInfo 中获取对应的companyId
     * @return
     */
    public int getCompanyIdByEnterprise(Enterprise enterprise){
        String sql = String.format("select id from EnterpriseInfo where name = '%s'",enterprise.getName());
        //"select id from EnterpriseInfo where name = ?";
        if(enterprise.getChuziqiye()!=null)sql += " and chuziqiye = ?";
        if(enterprise.getZhuceziben()!=null)sql += " and zhuceziben = ?";
        if(enterprise.getHangye()!=null)sql += " and hangye = ?";
        if(enterprise.getZhuceriqi()!=null)sql += " and zhuceriqi = ?";
        if(enterprise.getCode()!=null)sql += " and code = ?";
        if(enterprise.getType()!=null)sql += " and type = ?";
        if(enterprise.getZuzhixingshi()!=null)sql += " and zuzhixingshi = ?";
        if(enterprise.getLevel()!=null)sql += " and level = ?";
        if(enterprise.getZhucedi()!=null)sql += " and zhucedi = ?";

        if(enterprise.getChuziqiye()==null)sql += " and chuziqiye is NULL";
        if(enterprise.getZhuceziben()==null)sql += " and zhuceziben is NULL";
        if(enterprise.getHangye()==null)sql += " and hangye is NULL";
        if(enterprise.getZhuceriqi()==null)sql += " and zhuceriqi is NULL";
        if(enterprise.getCode()==null)sql += " and code is NULL";
        if(enterprise.getType()==null)sql += " and type is NULL";
        if(enterprise.getZuzhixingshi()==null)sql += " and zuzhixingshi is NULL";
        if(enterprise.getLevel()==null)sql += " and level is NULL";
        if(enterprise.getZhucedi()==null)sql += " and zhucedi is NULL";
        //String sql = "select id from EnterpriseInfo where name = ? and chuziqiye = ? and zhuceziben = ? and hangye = ? and zhuceriqi = ? and code = ? and type = ? and zuzhixingshi = ? and level = ? and zhucedi = ?";
        //防止出现有重复记录的情况
        List<Integer> list =  (List<Integer>) jdbcTemplate.query(sql, new RowMapper<Integer>(){
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int companyId = rs.getInt("id");
                return companyId;
            }
        },enterprise.getSQL());//enterprise.getName(),enterprise.getChuziqiye(),enterprise.getZhuceziben(),enterprise.getHangye(),enterprise.getZhuceriqi(),enterprise.getCode(),enterprise.getType(),enterprise.getZuzhixingshi(),enterprise.getLevel(),enterprise.getZhucedi());
        if(list.size()>0)//否则没查到会报出异常
            return list.get(0);
        return 0;
    }

    public void updateCompanyAlias(int companyId,String aliasName){
        jdbcTemplate.update(String.format("INSERT INTO CompanyAlias VALUES(%d,'%s')",companyId,aliasName));
    }
}
