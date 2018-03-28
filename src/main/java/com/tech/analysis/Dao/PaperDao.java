package com.tech.analysis.Dao;

import com.tech.analysis.entity.AddressTemp;
import org.neo4j.cypher.internal.frontend.v2_3.ast.Add;
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
//        List<AddressTemp> addressTemps = jdbcTemplate.query(sql, new RowMapper<AddressTemp>(){
//            @Override
//            public AddressTemp mapRow(ResultSet rs, int rowNum) throws SQLException {
//                AddressTemp addressTemp = new AddressTemp();
//                addressTemp.setUid(rs.getString("UID"));
//                addressTemp.setOrganization(rs.getString("organization"));
//                addressTemp.setSuborganization(rs.getString("suborganization"));
//                addressTemp.setFull_address(rs.getString("full_address"));
//                addressTemp.setCity(rs.getString("city"));
//                addressTemp.setCountry(rs.getString("country"));
//                addressTemp.setZip(rs.getString("zip"));
//                addressTemp.setAddr_no(rs.getInt("addr_no"));
//                return addressTemp;
//            }
//        },name);
        List<AddressTemp> addressTemps = getAddressTemps(sql,name);
        return addressTemps;
    }

    /**
     * @param companyId  对Address 进行插入
     * @param
     */
    public void updateAddress(int companyId,AddressTemp addressTemp){
        jdbcTemplate.update("INSERT INTO Address VALUES('"
                + addressTemp.getUid() + "', '"
                + companyId + "', '"
                + addressTemp.getAddr_no() + "', '"
                + addressTemp.getOrganization() + "', '"
                + addressTemp.getSuborganization() + "', '"
                + addressTemp.getFull_address() + "', '"
                + addressTemp.getCity() + "', '"
                + addressTemp.getCountry() + "', '"
                + addressTemp.getZip() + "')");
    }

    /**
     * @param addressTemp 将需要回退的AddressTemp插入到AddressTemp表中
     */
    public void updateAddressTemp(AddressTemp addressTemp){
        jdbcTemplate.update(addressTemp.getInsertSql());
    }

    public void deleteItemInAddressTempByOrganization(String organization){
        jdbcTemplate.update(String.format("delete from AddressTemp where organization = '%s'",organization));
    }

    public List<String> getAddressTempNames(){
        String sql = "select organization from AddressTemp";
        List<String> names = jdbcTemplate.query(sql, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String name = rs.getString("organization");
                return name;
            }
        });
        return names;
    }

    public List<AddressTemp> getAddressTempsInAddress(int companyId,String aliasName){
        String sql = "SELECT UID,organization,suborganization,full_address,city,country,zip,addr_no FROM Address where companyid = ? and organization = ?";
        List<AddressTemp> addressTemps = getAddressTemps(sql,companyId,aliasName);
        return addressTemps;
    }

    /**
     * @param sql 根据sql语句去获取AddressTemp实体
     * @param args
     * @return
     */
    public List<AddressTemp> getAddressTemps(String sql,Object... args){
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
                addressTemp.setAddr_no(rs.getString("addr_no"));
                return addressTemp;
            }
        },args);
        return addressTemps;
    }

    /**
     * @param table  Address or AddressTemp
     * @param companyid
     * @param aliasName
     */
    public void deleteItemByCompanyIdAndAliasname(String table,int companyid,String aliasName){
        jdbcTemplate.update(String.format("delete from %s where companyid = %d and organization = '%s'",table,companyid,aliasName));
    }
}
