package com.tech.analysis.controller;

import com.tech.analysis.entity.Enterprise;
import com.tech.analysis.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/21 0021.
 */
@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private MatchService matchService;
    @RequestMapping("/getEnterprise")
    public List<Enterprise> getEnterprise(@RequestBody Map<String,Object> map){
        String source = (String) map.get("source");//数据来源
        List<Enterprise> list = new ArrayList<>();
        if(source.equals("paper")){
            list = matchService.getEnterpriseListOfSimilarName((String) map.get("aliasName"));
        }
        return  list;
    }
    @RequestMapping("/update")
    public Enterprise updateEnterprise(@RequestBody Map<String,Object> map){

        //构造Enterprise实体，数据来源，机构名；找到该机构实体对应的EntepriseInfo 中的id
        Enterprise enterprise = matchService.setEnterpriseByRequest(map);
        String source = (String) map.get("source");//数据来源
        String aliasName = (String) map.get("aliasName");//机构名
        //如果数据来源是论文
        if(source.equals("paper")){
            //根据机构名去temp表中查该机构发表的论文（可能多篇）,
            // 将该机构发的论文对应到Address表中，将该机构名和对应id存入别名中
            matchService.updatePaper(enterprise,aliasName);
        }
        return enterprise;
    }

}
