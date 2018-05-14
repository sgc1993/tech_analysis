package com.tech.analysis.controller;

import com.tech.analysis.entity.Enterprise;
import com.tech.analysis.service.KeywordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by XCY on 2018/3/26.
 */
@RestController
@RequestMapping("/graph")
public class KeywordsController {
    @Autowired
    private KeywordsService keywordsService;
//    private KeywordsService keywordsService = new KeywordsService();
    @RequestMapping("/getKeywords")
    //得到精确查找字符串
    public String getKeywords(@RequestParam String target){
        //String target = (String) map.get("target");//目标词汇

        System.out.println(target);
        return keywordsService.getTargetDependKeywords(target);
    }

    @RequestMapping("/yearKeywords")//年份
    //得到历年关键词图
    public List<String> getKeywordsList(){
        List<String> ans = keywordsService.getYearGraph();
        return  ans;
    }
    @RequestMapping("/predict")
    //得到预测结果
    public String predict(){
        return null;
    }


}
