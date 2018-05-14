package com.tech.analysis.service;

import com.tech.analysis.Dao.*;
import com.tech.analysis.util.ConvertUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17 0017.
 */
@Service
public class GetKeyphraseSevice {

    @Autowired
    private PaperDao paperDao;
    @Autowired
    private PatentDao patentDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ExpertDao expertDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private ConvertUtil convertUtil;

    public void updateKeyphraseForPaper(){
//        //1.获取所有has_keyword字段为0的论文UID列表
//        List<String> uidList = paperDao.getUidList();
//        //2.对于每个uid对该论文的abstract_text_cn内容抽取关键词，存放进去
//        for (String uid : uidList) {
//            paperDao.updatePaperKeywordsByUid(uid);
//        }
        paperDao.updateKeyphraseForPaper();
    }

    public void updateKeyphraseForPatent(){

        //1.获取所有has_keyword字段为0的专利id列表
        List<String> idList = patentDao.getidList();
        //2.对每个id对该专利的abstract_cn抽取关键词，存放
        for (String id : idList) {
            patentDao.updatePatentKeywordsByid(id);
        }
    }

    public void updateKeyphraseForProject(){
        //1.获取所有has_keyword字段为0的专利id列表
        List<String> idList = projectDao.getidList();
        //2.对每个id对该专利的abstract_cn抽取关键词，存放
        for (String id : idList) {
            projectDao.updateProjectKeywordsByid(id);
        }
    }

    public void getKeyphraseForExpert(){

        //1、获取所有的专家的id
        List<String> expertIdList  =  expertDao.getIdList();
        //2、对每一个专家进行合并关键词
        for (String expertId : expertIdList) {
            List<String> keywordsList = expertDao.getKeywordByExpertId("paper",expertId);
            keywordsList.addAll(expertDao.getKeywordByExpertId("patent",expertId));
            keywordsList.addAll(expertDao.getKeywordByExpertId("project",expertId));
            JSONObject obj = convertUtil.getKeywordJsonByStringList(keywordsList);
            expertDao.putKeywordByExpertId(expertId,obj.toString());
        }

    }

    public void getKeyphraseForEnterprise(){
        //1、获取所有企业id
        List<String> enterpriseIdList = enterpriseDao.getAllEnterpriseIdList();
        //2、对于每一个企业id合并出它的关键词
        for (String enterpriseId : enterpriseIdList) {
            List<String> keywordsList = enterpriseDao.getKeywordsByEnterpriseId(enterpriseId);
            JSONObject obj = convertUtil.getKeywordJsonByStringList(keywordsList);
            enterpriseDao.putKeywordByEnterpriseId(enterpriseId,obj.toString());
        }
    }

}
