package com.tech.analysis.service;

import com.sun.xml.internal.bind.v2.runtime.output.Encoded;
import com.tech.analysis.Dao.EnterpriseDao;
import com.tech.analysis.Dao.MatchDao;
import com.tech.analysis.Dao.PaperDao;
import com.tech.analysis.entity.AddressTemp;
import com.tech.analysis.entity.Enterprise;
import com.tech.analysis.util.MatchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/21 0021.
 */
@Service
public class MatchService {

    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private PaperDao paperDao;
    @Autowired
    private MatchDao matchDao;
    @Autowired
    private MatchUtil matchUtil;

    /**
     * @param id 根据一个id获取对应企业实体
     * @return
     */
    public Enterprise getEnterpriseById(int id){
        return enterpriseDao.getEnterpriseById(id);
    }


    /**
     * @param enterpriseName 根据一个企业名称，获取和其可能对应的机构信息
     * @return 企业实体列表
     */
    public List<Enterprise> getEnterpriseListOfSimilarName(String enterpriseName){

        List<Enterprise> list = new ArrayList<>();
        //获得所有企业实体
        List<Enterprise> enterpriseList = enterpriseDao.getAllEnterpriseList();
        //如果该机构名称是汉语的
        if(matchUtil.isChinese(enterpriseName)){
            //将所有企业列表和该企业名传进去，返回所有和该企业名相似的企业列表
            list = matchUtil.getSimEnterpriseList(enterpriseName,enterpriseList);
        }else{//机构名是英语的情况
            //创建一个队列，用于存放该英文机构对应的可能中文机构
            List<String> maybeChineseNameList = new ArrayList<>();
            //在temp表中获取该英文机构的UID（可能不止一个，取一个即可包含）
            String paperUID = paperDao.getUidByName(enterpriseName);
            //在temp表中查包含该论文的中文机构名，加入队列
            List<String> nameListFromAddressTemp = paperDao.getNameListByUid(paperUID,"AddressTemp");
            //用UID去Address表中查找该论文对应的机构名，如果是中文的，加入队列
            List<String> nameListFromAddress = paperDao.getNameListByUid(paperUID,"Address");
            //将该机构对应的几个可能的中文名，按汉语相似机构匹配方式匹配
            nameListFromAddress.addAll(nameListFromAddressTemp);
            HashSet<String> hashSet = new HashSet<>(nameListFromAddress);
            nameListFromAddress.clear();
            nameListFromAddress.addAll(hashSet);
            for (String name:nameListFromAddress) {
                if(matchUtil.isChinese(name))
                    maybeChineseNameList.add(name);
            }
            for (String name:maybeChineseNameList) {
                List<Enterprise> alist = matchUtil.getSimEnterpriseList(name,enterpriseList);
                list.addAll(alist);
            }
            //对可能的多个中文名匹配的相似机构可能重复，去重
            HashSet<Enterprise> enterprisesSet = new HashSet<>(list);
            list.clear();
            list.addAll(enterprisesSet);
        }
        return  list;
    }

    /**
     * @param map  根据请求参数构造Enterprise实体
     * @return Enterprise实体
     */
    public Enterprise setEnterpriseByRequest(Map<String,Object> map){
        Enterprise enterprise = new Enterprise();
        enterprise.setName((String)map.get("name"));
        enterprise.setChuziqiye((String)map.get("chuziqiye"));
        //不知道前端传来的数据是Integer还是Long（会根据数值大小变化），
        // Integer 不能直接转换为Long,他们没有继承关系
//        Number num = (Number) map.get("zhuceziben");
//        Long zhuceziben;
//        if(num!=null){
//            zhuceziben = num.longValue();
//        }else{
//            zhuceziben = null;
//        }
        enterprise.setZhuceziben((String) map.get("zhuceziben"));
        enterprise.setHangye((String)map.get("hangye"));
        enterprise.setZhuceriqi((String)map.get("zhuceriqi"));
        enterprise.setCode((String)map.get("code"));
        enterprise.setType((String)map.get("type"));
        enterprise.setZuzhixingshi((String)map.get("zhucexingshi"));
        enterprise.setLevel((String)map.get("level"));
        enterprise.setZhucedi((String)map.get("zhucedi"));
        return enterprise;
    }

//    /**
//     * @param enterprise 根据前端选择的企业名称和对应企业信息，更新数据库表内容，匹配机构别名
//     * @param aliasName AddressTemp 表中的organization名
//     */
//    public void updatePaper(Enterprise enterprise,String aliasName){
//        //1、根据organization==aliasName去AddressTemp表中匹配出该机构的所有论文List<Address>
//        List<AddressTemp> addressTemps = paperDao.getAddressTempsByName(aliasName);
//        //2、根据选中的企业实体enterprise去EnterpriseInfo中匹配得到companyid
//        int companyId = enterpriseDao.getCompanyIdByEnterprise(enterprise);
//        for (AddressTemp a:addressTemps
//                ) {
//            System.out.print(a);
//        }
//        //3、将AddressTemp和companyid插入Address表中
//        updateAddress(companyId,addressTemps);
//        //4、将AddressTemp中对应aliasName记录删除
//        paperDao.deleteItemInAddressTempByOrganization(aliasName);
//        //5、向CompanyAlias中插入企业别名和对应companyid
//        enterpriseDao.updateCompanyAlias(companyId,aliasName);
//        //注意他的数据库里id字段不是自增的
//    }

    public void updatePaper(int companyId,String aliasName){
        //1、根据organization==aliasName去AddressTemp表中匹配出该机构的所有论文List<Address>
        List<AddressTemp> addressTemps = paperDao.getAddressTempsByName(aliasName);
        //3、将AddressTemp和companyid插入Address表中
        updateAddress(companyId,addressTemps);
        //4、将AddressTemp中对应aliasName记录删除
        paperDao.deleteItemInAddressTempByOrganization(aliasName);
        //5、向CompanyAlias中插入企业别名和对应companyid
        enterpriseDao.updateCompanyAlias(companyId,aliasName);
        //注意他的数据库里id字段不是自增的
        //往匹配记录表MatchRecord里记录匹配数据，以备后序撤销匹配操作
        matchDao.updateMatchRecord(companyId,aliasName,"paper");

    }


    /**
     * @param companyId 对选出来的数据进行插入
     * @param addressTemps
     * @return
     */
    public void updateAddress(int companyId,List<AddressTemp> addressTemps){
        for (AddressTemp addressTemp:
             addressTemps) {
            paperDao.updateAddress(companyId,addressTemp);
        }
    }

    public List<String> getWaitForMatch(String source){
        List<String> names = new ArrayList<>();
        if(source.equals("paper")){
            names = paperDao.getAddressTempNames();
        }
        return names;
    }

    /**
     * @param companyId 对曾今插入过的数据进行回滚
     * @param aliasName
     * @param source
     */
    public void rollbackMatch(int companyId,String aliasName,String source){
        if(source.equals("paper")){
            //可能这个机构对应的多条论文都被错插了，所以aliasName，companyId在Address中可能会匹配到多条一致的AddressTemp需要插入
            //1、从Address中获取对应的AddressTemp数据
            List<AddressTemp> addressTemps = paperDao.getAddressTempsInAddress(companyId,aliasName);
            //2、将获取到的数据逐条插入到Temp表中
            updateAddressTemp(addressTemps);
            //3、将Address对应的原有的数据删除
            paperDao.deleteItemByCompanyIdAndAliasname("Address",companyId,aliasName);
            //4、将CompanyAlias中的对应数据删除
            matchDao.deleteItemByCompanyIdAndAliasname("CompanyAlias",companyId,aliasName);
            //5、MatchRecord表中去除该匹配记录
            matchDao.deleteItemByCompanyIdAndAliasname("MatchRecord",companyId,aliasName);
        }
    }

    public void updateAddressTemp(List<AddressTemp> addressTemps){
        for (AddressTemp addressTemp : addressTemps) {
            paperDao.updateAddressTemp(addressTemp);
        }
    }
}
