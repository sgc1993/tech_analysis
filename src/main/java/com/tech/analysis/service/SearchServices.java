package com.tech.analysis.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tech.analysis.Dao.WordModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchServices
{
    private static Logger logyangqi = LoggerFactory.getLogger("yxjyangqi");
    private static Logger logpaper = LoggerFactory.getLogger("yxjpaper");
    private  static Directory directory=null;
    private  static IndexReader reader=null;

    public static JsonObject searchYangqi(String[] keywords)
    {//
        WordModel get_word=new WordModel();
        List<String> list = new ArrayList<String>();
        JsonObject obj=new JsonObject();
        JsonArray array=new JsonArray();
        try {
            directory = FSDirectory.open(new File("F:\\IdeaProjects\\qq1\\qq\\yangqii_index_test"));
            reader = IndexReader.open(directory);
        }catch (Exception e)
        {
            System.out.println(e);
        }
        int len;

        len=keywords.length;
//        len=2;

        for (String str:keywords)
        {
            list.add(str);
        }
        for(int i=0;i<len;i++)
        {
            List<String> temp=new ArrayList<String>();
            try
            {

                String x=keywords[i].replace("\"","");
                logyangqi.info("导入字符串："+x);
                temp=get_word.distance(x);
            }catch (Exception e)
            {
                logyangqi.info(e.toString());
            }
            if(temp!=null)
            {
                for(String x:temp)
                {
                    list.add(x);
                }
            }

        }
        logyangqi.info("新关键字长度"+list.size());
        int newlen=list.size();
        String[] newkeywords=new String[newlen];
        if(newlen!=0)
        {
            list.toArray(newkeywords);
            for (String y:newkeywords)
            {
                logyangqi.info("key新关键字遍历"+y);
            }
        }
        newlen=newkeywords.length;
        logyangqi.info("新的长度"+newlen);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        indexSearcher.setSimilarity(new IKSimilarity());
        TopDocs topDocs=null;
        if(newlen==1)
        {
            logyangqi.info("进入央企单字段查询");
            try {
                Query query1 = IKQueryParser.parse("yangqikeywords",keywords[0]);//不支持不分词的filed进行查询
                topDocs = indexSearcher.search(query1,null, 10);
            }catch (Exception e)
            {
                logyangqi.error("查找数据失败");
            }

        }
        else {
            logyangqi.info(""+newlen);
            logyangqi.info("进入央企多字段查询");
            BooleanClause.Occur[] clauses = new BooleanClause.Occur[newlen];
            for (int i=0;i<newlen;i++)
            {
                clauses[i]=BooleanClause.Occur.SHOULD;
            }
            try{
                String[] searchField=new String[newlen];
                for (int i=0;i<newlen;i++)
                    searchField[i]="yangqikeywords";
                logyangqi.info(""+searchField.length);
//                String[] q={"测量区域亮度","右眼通道图像"};
                String[] q=newkeywords;
                Query query1 = IKQueryParser.parseMultiField(searchField,q,clauses);//不支持不分词的filed进行查询
                topDocs = indexSearcher.search(query1,null, 10);
            }catch (Exception e)
            {
                logyangqi.error(e.toString());
                logyangqi.error("查找数据失败");
            }
        }
        try {
            logyangqi.info("结果总数"+topDocs.totalHits);
            for(ScoreDoc scoreDoc:topDocs.scoreDocs){
                JsonObject temp=new JsonObject();
                Document doc=indexSearcher.doc(scoreDoc.doc);
//                log.info(doc.get("yangqikeywords"));
//                log.info(doc.get("yangyiname"));
//                log.info(doc.get("yangqiaddress"));
                temp.addProperty("央企名称",doc.get("yangyiname"));
                temp.addProperty("央企地址",doc.get("yangqiaddress"));
                array.add(temp);
//                log.info(array.toString());
                logyangqi.info("成功");
            }
            obj.add("result",array);
            logyangqi.info(obj.toString());
        }catch (Exception e)
        {
            logyangqi.error("数据封装JSON失败");
        }

        return obj;
    }

    public static JsonObject searchpaper(String[] keywords,int year)
    {
        logpaper.info("查找论文");
        WordModel get_word=new WordModel();
        List<String> list = new ArrayList<String>();
        JsonObject obj=new JsonObject();
        int papernum=0,patentnum=0,prizenum=0;
        try {
            directory = FSDirectory.open(new File("F:\\IdeaProjects\\qq1\\qq\\paper_index"));
            reader = IndexReader.open(directory);
        }catch (Exception e)
        {
            logpaper.error("查找索引失败");
        }
        int len;
        int currentyear=2018;
        int range=(currentyear-year)*3;
        int[] num=new int[range];
        len=keywords.length;
        logpaper.info("长度"+len);
        for (String str:keywords)
        {
            list.add(str);
        }
        for(int i=0;i<len;i++)
        {
            List<String> temp=new ArrayList<String>();
            try
            {

                String x=keywords[i].replace("\"","");
                logpaper.info("导入字符串："+x);
                temp=get_word.distance(x);
            }catch (Exception e)
            {
                logpaper.info(e.toString());
            }
            if(temp!=null)
            {
                for(String x:temp)
                {
                    list.add(x);
                }
            }

        }
        logpaper.info("新关键字长度"+list.size());
//        for (String x:list)
//        {
//            log.info("新关键字遍历"+x);
//        }
        int newlen=list.size();
        String[] newkeywords = new String[newlen];
        if(newlen!=0)
        {

            list.toArray(newkeywords);
            for (String y:newkeywords)
            {
                logpaper.info("key新关键字遍历"+y);
            }
        }
        newlen=newkeywords.length;
        logpaper.info("新的长度"+newlen);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        indexSearcher.setSimilarity(new IKSimilarity());
        TopDocs topDocs=null;
        if(newlen==1)
        {
            logpaper.info("进入单字段查询");
            try {
                Query query1 = IKQueryParser.parse("paperkeywords",keywords[0]);//不支持不分词的filed进行查询
                topDocs = indexSearcher.search(query1,null, 10);
                logpaper.info(""+topDocs.totalHits);
            }catch (Exception e)
            {
                logpaper.error("查找数据失败");
            }

        }
        else {
            logpaper.info("进入多字段查询");
//            BooleanClause.Occur[] clauses = { BooleanClause.Occur.MUST, BooleanClause.Occur.MUST };
            BooleanClause.Occur[] clauses = new BooleanClause.Occur[newlen];
            for (int i=0;i<newlen;i++)
            {
                clauses[i]=BooleanClause.Occur.SHOULD;
            }
            try{
                String[] searchField=new String[newlen];
                for (int i=0;i<newlen;i++)
                    searchField[i]="paperkeywords";
//                String[] q={"高精度","高质量"};
                String[] q=newkeywords;
                Query query1 = IKQueryParser.parseMultiField(searchField,q,clauses);//不支持不分词的filed进行查询
                topDocs = indexSearcher.search(query1,null, 10);
            }catch (Exception e)
            {
                logpaper.error("查找数据失败");
            }
        }
        try {
            logpaper.info("结果总数"+topDocs.totalHits);
            for(ScoreDoc scoreDoc:topDocs.scoreDocs){
                Document doc=indexSearcher.doc(scoreDoc.doc);
                logpaper.info(doc.get("paperkeywords"));
                logpaper.info(doc.get("pubyear"));
                logpaper.info(doc.get("label"));
                int pubyear=Integer.valueOf(doc.get("pubyear"));
                logpaper.info("年份"+pubyear);
                if(pubyear>=year&&pubyear<=currentyear)
                {
                    logpaper.info("查询年份"+pubyear);
                    logpaper.info("进入");
                    int temp=pubyear-year;
                    if(doc.get("label").charAt(0)=='C')
                        num[temp*3]=++num[temp*3];
                    if(doc.get("label").charAt(0)=='F'||doc.get("label").charAt(0)=='Z')
                        num[temp*3+1]=++num[temp*3+1];
                    if(doc.get("label").charAt(1)=='N')
                        num[temp*3+2]=++num[temp*3+2];
                }

            }
            int x=currentyear-year;
            for (int i=0;i<x;i++)
            {
                JsonObject temp=new JsonObject();
                temp.addProperty("papernum",num[i*3]);
                temp.addProperty("prizenum",num[i*3+1]);
                temp.addProperty("patentnum",num[i*3+2]);
                int newyear1=i+year;
                String newyear=""+newyear1;
//                log.info(temp.toString());
//                log.info(newyear);
//                log.info(temp.toString());
                obj.add(newyear,temp);
            }
            logpaper.info(obj.toString());
        }catch (Exception e)
        {
            logpaper.error("数据封装JSON失败");
        }

        return obj;
    }
}
