package com.tech.analysis.service;
import com.tech.analysis.Dao.KeywordsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by XCY on 2018/3/26.
 */
@Service

public class KeywordsService {
    @Autowired
    private KeywordsDao keywordsDao;
    /**
     * @param target 精查找的词语
     * @return 查找到的json字符串
     */
    public String getTargetDependKeywords(String target){
        return keywordsDao.getJsonString(target);
    }
}
