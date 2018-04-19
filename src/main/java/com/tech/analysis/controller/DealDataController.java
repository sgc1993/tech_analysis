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
    public void get(){
        dealDataService.buildSimiliarModel();
    }
}
