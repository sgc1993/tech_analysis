package com.tech.analysis.Dao;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * 用于操作匹配过程中的操作表,主要操作表CompanyAlias,和MatchRecord
 * */
@Repository
public class MatchDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateMatchRecord(int companyId,String aliasName,String source){
        Date now = new Date();
        DateFormat df = DateFormat.getDateInstance();
        String nowTime = df.format(now);
        jdbcTemplate.update(String.format("insert into MatchRecord values(%d,'%s','%s','%s')",companyId,aliasName,source,nowTime));
    }

    public void deleteItemByCompanyIdAndAliasname(String table,int companyid,String aliasName){
        jdbcTemplate.update(String.format("delete from %s where companyid = %d and aliasName = '%s'",table,companyid,aliasName));
    }
}
