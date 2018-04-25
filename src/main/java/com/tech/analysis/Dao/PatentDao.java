package com.tech.analysis.Dao;

import com.tech.analysis.entity.AddressTemp;
import com.tech.analysis.entity.PatentIdAndEnterpriseNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2018/3/29 0029.
 */
@Repository
public class PatentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * @return 从patent数据表中抽出patentid,enterpriseNames对
     */
    public List<PatentIdAndEnterpriseNames> getpatentIdAndEnterpriseNames(){
        String sql = "select id,enterprisename from patent";
        List<PatentIdAndEnterpriseNames> list = jdbcTemplate.query(sql, new RowMapper<PatentIdAndEnterpriseNames>(){
            @Override
            public PatentIdAndEnterpriseNames mapRow(ResultSet rs, int rowNum) throws SQLException {
                PatentIdAndEnterpriseNames patentIdAndEnterpriseNames = new PatentIdAndEnterpriseNames();
                patentIdAndEnterpriseNames.setPatentId(rs.getString("id"));
                String enterpriseName =  rs.getString("enterprisename");
                String[] names = enterpriseName.split("\\|");
                for(int i = 0;i < names.length;i++){
                    names[i] = names[i].trim();
                }
                patentIdAndEnterpriseNames.setEnterpriseNames(names);
                return patentIdAndEnterpriseNames;
            }
        });
        return list;
    }

    public List<String> getEnterpriseIdListByPatentId(String patentId){
        String sql = "select enterpriseid from Patent2EnterpriseNew where patentid = ?";
        List<String> list = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String enterpriseId = rs.getString("enterpriseid");
                return enterpriseId;
            }
        },patentId);
        return list;
    }

    public void putPatentToPatentForMatchTable(String patentId,String enterpriseName){
        jdbcTemplate.update(String.format("INSERT INTO patentForMatchNew VALUES('%s','%s')",patentId,enterpriseName));
        //备份这个表，在撤销操作时方便操作
        jdbcTemplate.update(String.format("INSERT INTO patentForMatchBackup VALUES('%s','%s')",patentId,enterpriseName));
    }

    public List<String> getPatentIdListByEnterpriseName(String table, String enterpriseName){
        String sql = String.format("select patentid from %s where enterpriseName = '%s'",table,enterpriseName);
        List<String> idList = new ArrayList<>();
        idList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String id = rs.getString("patentid");
                return id;
            }
        });
        return idList;
    }

    public void updatePatent2Enterprise(String patentId,int companyId){
        jdbcTemplate.update(String.format("INSERT INTO patent2Enterprise VALUES('%s',%d)",patentId,companyId));
    }

    public void deleteItemByEnterpriseName(String table,String enterpriseName){
        jdbcTemplate.update(String.format("delete from %s where enterpriseName = '%s'",table,enterpriseName));
    }

    /**
     * @param patentId   在回滚专利机构人工选择时调用，将之前匹配后删除的记录重新添加回来
     * @param enterpriseName
     */
    public void updatePatentForMatch(String patentId,String enterpriseName){
        jdbcTemplate.update(String.format("INSERT INTO patentForMatchNew VALUES('%s','%s')",patentId,enterpriseName));
    }

    public void deleteItemInPatent2Enterprise(String patentId,int companyId){
        jdbcTemplate.update(String.format("delete from patent2enterprise where patentid = '%s' and enterpriseid = %d",patentId,companyId));
    }
}
