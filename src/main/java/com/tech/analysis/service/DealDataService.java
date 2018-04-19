package com.tech.analysis.service;

import com.tech.analysis.Dao.LoadWordAndVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 该类用于后台处理数据
 * Created by XCY on 2018/4/19.
 */

@Service
public class DealDataService {
    @Autowired
    private LoadWordAndVector loadWordAndVector;

    /**
     * 生成OriginalData.dat  内容是摘要和标题
     */
    public void buildSimiliarModel(){
        loadWordAndVector.buildModel();
    }
}
