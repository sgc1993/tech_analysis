package com.tech.analysis.controller;

import com.tech.analysis.service.DealDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by XCY on 2018/4/19.
 */
@RestController
@RequestMapping("/model")
public class DealDataController {
    @Autowired
    private DealDataService dealDataService;

    @RequestMapping("/similiarModel")
    //得到预测结果

    /**
     * 构建计算相似度的模型
     */
    public void get(){
        dealDataService.buildSimiliarModel();
    }

    @RequestMapping("/buildCSV")
    //得到预测结果
    /**
     * 构建导入neo4j的keywords和relationship 的 CSV文件
     */
    public void getCSV(){
        dealDataService.getKeyWordsAndRelationCSV();
    }

    @RequestMapping("/PredictionKeywords")
    /**
     * 根据历史预测关键字
     */
    public void PredictionKeywords(){
        dealDataService.getPredictionKeywords();
    }

}
