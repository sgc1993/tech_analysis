package com.tech.analysis.service;

import com.tech.analysis.Dao.EnterpriseDao;
import com.tech.analysis.entity.Enterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21 0021.
 */
@Service
public class MatchService {

    @Autowired
    private EnterpriseDao enterpriseDao;

    public Enterprise getEnterpriseById(int id){
        return enterpriseDao.getEnterpriseById(id);
    }

    public List<Enterprise> getList(){
        return enterpriseDao.getList();
    }
}
