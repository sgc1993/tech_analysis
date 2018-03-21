package com.tech.analysis.Dao;

import com.tech.analysis.entity.Enterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public List<Enterprise> getList(){
        String sql = "SELECT name FROM EnterpriseInfo where id > 3000 and id < 3500";
        List<Enterprise> list =  (List<Enterprise>) jdbcTemplate.query(sql, new RowMapper<Enterprise>(){

            @Override
            public Enterprise mapRow(ResultSet rs, int rowNum) throws SQLException {
                Enterprise enterprise = new Enterprise();
                enterprise.setName(rs.getString("name"));
                return enterprise;
            }

        });
        System.out.println(list.size());
        return  list;
    }

}
