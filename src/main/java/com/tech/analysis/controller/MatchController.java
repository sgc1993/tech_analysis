package com.tech.analysis.controller;

import com.tech.analysis.entity.Enterprise;
import com.tech.analysis.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21 0021.
 */
@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private MatchService matchService;
    @RequestMapping("/getEnterprise")
    public List<Enterprise> getEnterprise(){
        List<Enterprise> list = matchService.getList();
        //System.out.print(list.get(2).getName());
        return  list;
    }
    @RequestMapping("/getEnterpriseById")
    @ResponseBody
    public Enterprise getById(){
        return matchService.getEnterpriseById(4988);
    }
}
