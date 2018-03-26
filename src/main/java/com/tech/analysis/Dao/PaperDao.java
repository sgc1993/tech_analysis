package com.tech.analysis.Dao;

import com.tech.analysis.entity.AddressTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/3/22 0022.
 */
@Repository
public class PaperDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * @param name 根据机构名字去AddressTemp表中找到论文对应UID
     * @return 找到该英文机构发表的某篇论文的UID
     */
    public String getUidByName(String name){

        String sql = "select UID from AddressTemp where organization = ?";
        List<String> uidList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("UID");
            }
        },name);
        return uidList.get(0);
    }

    /**
     * @param uid 根据UID去table中去查找该论文的作者机构
     * @return 返回机构名列表
     */
    public List<String> getNameListByUid(String uid,String table){
        String sql = String.format("SELECT organization from %s where UID = ?", table);
        List<String> nameList = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("organization");
            }
        },uid);
        return nameList;
    }

    /**
     * @param name 根据企业名称获取AddressTemp 实体
     * @return
     */
    public List<AddressTemp> getAddressTempsByName(String name){
        String sql = "SELECT UID,organization,suborganization,full_address,city,country,zip,addr_no FROM AddressTemp where organization = ?";
        List<AddressTemp> addressTemps = jdbcTemplate.query(sql, new RowMapper<AddressTemp>(){
            @Override
            public AddressTemp mapRow(ResultSet rs, int rowNum) throws SQLException {
                AddressTemp addressTemp = new AddressTemp();
                addressTemp.setUid(rs.getString("UID"));
                addressTemp.setOrganization(rs.getString("organization"));
                addressTemp.setSuborganization(rs.getString("suborganization"));
                addressTemp.setFull_address(rs.getString("full_address"));
                addressTemp.setCity(rs.getString("city"));
                addressTemp.setCountry(rs.getString("country"));
                addressTemp.setZip(rs.getString("zip"));
                addressTemp.setAddr_no(rs.getInt("addr_no"));
                return addressTemp;
            }
        },name);
        return addressTemps;
    }

    /**
     * @param companyId  对Address 进行插入
     * @param addressTemps
     */
    public void updateAddress(int companyId,AddressTemp addressTemps){
        jdbcTemplate.update("INSERT INTO Address VALUES('"
                + addressTemps.getUid() + "', '"
                + companyId + "', '"
                + addressTemps.getAddr_no() + "', '"
                + addressTemps.getOrganization() + "', '"
                + addressTemps.getSuborganization() + "', '"
                + addressTemps.getFull_address() + "', '"
                + addressTemps.getCity() + "', '"
                + addressTemps.getCountry() + "', '"
                + addressTemps.getZip() + "')");
    }

    public void deleteItemInAddressTempByOrganization(String organization){
        jdbcTemplate.update(String.format("delete from AddressTemp where organization = '%s'",organization));
    }
}
