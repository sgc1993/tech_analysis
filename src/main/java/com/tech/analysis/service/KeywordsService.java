package com.tech.analysis.service;
import com.tech.analysis.Dao.KeywordsDao;
import com.tech.analysis.Dao.LoadWordAndVector;
import com.tech.analysis.Dao.CreatGraphAboutYear;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by XCY on 2018/3/26.
 */
@Service

public class KeywordsService {
    @Autowired
    private KeywordsDao keywordsDao;

    @Autowired
    private CreatGraphAboutYear creatGraphAboutYear;
    /**
     * @param target 精查找的词语
     * @return 查找到的json字符串
     */
    public String getTargetDependKeywords(String target){
        return keywordsDao.getJsonString(target);
    }

    /**
     *
     * @return 返回5年的图谱
     */
    public List<String> getYearGraph(){
        return creatGraphAboutYear.creatAll();
    }

}
