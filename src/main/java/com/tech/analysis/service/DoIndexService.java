package com.tech.analysis.service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

@Service
public class DoIndexService {
    public static  String creatIndex()
    {
        final String cfn = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        final  String url = "jdbc:sqlserver://localhost:1433;DatabaseName=TEST";
        String status="建立索引成功";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet res = null;
        System.out.println("读取数据库数据并索引"+res);
        long start = System.currentTimeMillis();
        int i=0;
        File indexDir =   new  File( "F:\\IdeaProjects\\lucene\\index" );
        try {
            Class.forName(cfn);
            con = DriverManager.getConnection(url,"sa","ilyxjin405405");
            String sql = "SELECT\n" +
                    "\tPersons.name,\n" +
                    "\tPersons.enterprisename,\n" +
                    "\tPersons.education,\n" +
                    "\tPersons.functionname,\n" +
                    "\tPersons.type,\n" +
                    "\tPersons.profield,\n" +
                    "\tPersons.sex,\n" +
                    "\tPersons.study_result,\n" +
                    "\tPersons.study_dir,\n" +
                    "\tPersons.keywords,\n" +
                    "\tD.address\n" +
                    "FROM\n" +
                    "    D\n" +
                    "INNER JOIN Persons ON (Persons.enterprisename = D.name)";//查询test表
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            System.out.println(res);
            Directory dir=FSDirectory.open(indexDir);
            IndexWriterConfig  iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
            IndexWriter writer=null;
            try
            {
                writer = new IndexWriter(dir,iwc);
            }catch (Exception e)
            {
                System.out.println("创建IndexWriter时发生错误!");
            }
            while(res.next()){
                i=i+1;
                System.out.println(i);
                String title = res.getString("name");//获取test_name列的元素                                                                                                                                                    ;
                System.out.println("姓名："+title);
                Document doc= getDocument(res);
                writer.addDocument(doc);
                System.out.println("索引创建完毕");
                System.out.print(System.currentTimeMillis() - start);
                System.out.println(" total milliseconds");
            }
            writer.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e);
            status="建立索引失败";
        }finally{
            try {
                if(res != null) res.close();
                if(statement != null) statement.close();
                if(con != null) con.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        return  status;
    }
    public static  String creat_yangqiIndex()
    {
        final String cfn = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        final  String url = "jdbc:sqlserver://10.168.103.8:1433;DatabaseName=STIMSTEST";
        String status="建立索引成功";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet res = null;
        System.out.println("读取数据库数据并索引"+res);
        long start = System.currentTimeMillis();
        int i=0;
        File indexDir =   new  File( "F:\\IdeaProjects\\qq1\\qq\\yangqii_index_test" );
        try {
            Class.forName(cfn);
            con = DriverManager.getConnection(url,"sa","1q2w3e4r5t!");
            String sql = "SELECT  [name]\n" +
                    "      ,[address]\n" +
                    "      ,[keywords]\n" +
                    "  FROM [STIMSTEST].[dbo].[EnterpriseInfo] where keywords is not null";//查询test表
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            System.out.println(res);
            Directory dir=FSDirectory.open(indexDir);
            IndexWriterConfig  iwc = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer());
            IndexWriter writer=null;
            try
            {
                writer = new IndexWriter(dir,iwc);
            }catch (Exception e)
            {
                System.out.println("创建IndexWriter时发生错误!");
            }
            while(res.next()){
                i=i+1;
                System.out.println(i);
                String title = res.getString("name");//获取test_name列的元素                                                                                                                                                    ;
                System.out.println("企业名字是："+title);
                Document doc= getyangqiDocument(res);
                writer.addDocument(doc);
                System.out.println("索引创建完毕");
                System.out.print(System.currentTimeMillis() - start);
                System.out.println(" total milliseconds");
            }
            writer.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e);
            status="建立索引失败";
        }finally{
            try {
                if(res != null) res.close();
                if(statement != null) statement.close();
                if(con != null) con.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        return  status;
    }

    public static  String creat_paperIndex()
    {
        final String cfn = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        final  String url = "jdbc:sqlserver://10.168.103.8:1433;DatabaseName=STIMSTEST";
        String status="建立索引成功";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet res = null;
        System.out.println("读取数据库数据并索引"+res);
        long start = System.currentTimeMillis();
        int i=0;
        File indexDir =   new  File( "F:\\IdeaProjects\\qq1\\qq\\paper_index_test" );
        try {
            Class.forName(cfn);
            con = DriverManager.getConnection(url,"sa","1q2w3e4r5t!");
            String sql = "select UID,keywords_cn  as name,pubyear from Paper where keywords_cn is not null \n" +
                    "union\n" +
                    "select code as UID,name,year as pubyear  from Prize \n" +
                    "union \n" +
                    "select patentnumber as UID,cast(keywords as nvarchar)as name,convert(varchar(4),success_date,120)as pubyear from Patent";//查询test表
            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            System.out.println(res);
            Directory dir=FSDirectory.open(indexDir);
            IndexWriterConfig  iwc = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer());
            IndexWriter writer=null;
            try
            {
                writer = new IndexWriter(dir,iwc);
            }catch (Exception e)
            {
                System.out.println("创建IndexWriter时发生错误!");
            }
            while(res.next()){
                i=i+1;
                System.out.println(i);
                String title = res.getString("name");//获取test_name列的元素                                                                                                                                                    ;
                System.out.println("文献关键字："+title);
                Document doc= getpaperDocument(res);
                writer.addDocument(doc);
                System.out.println("索引创建完毕");
                System.out.print(System.currentTimeMillis() - start);
                System.out.println(" total milliseconds");
            }
            writer.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e);
            status="建立索引失败";
        }finally{
            try {
                if(res != null) res.close();
                if(statement != null) statement.close();
                if(con != null) con.close();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        return  status;
    }
    public static Document getDocument(ResultSet res)throws Exception {
        Document doc=new Document();
        try{
            doc.add(new Field("name", res.getString("name"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("enterprisename", res.getString("enterprisename"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("education", res.getString("education"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("functionname", res.getString("functionname"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("zhucedi", res.getString("address"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("study_dir", res.getString("study_dir"),Field.Store.YES,Index.ANALYZED));
            doc.add(new Field("tech_area", res.getString("keywords"),Field.Store.YES,Index.ANALYZED));
            doc.add(new Field("type", res.getString("type"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("profield", res.getString("profield"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("sex", res.getString("sex"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("study_result", res.getString("study_result"),Field.Store.YES,Index.ANALYZED));
            System.out.println(("22222222"));
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return doc;
    }
    //给央企添加索引
    public static Document getyangqiDocument(ResultSet res)throws Exception {
        Document doc=new Document();
        try{
            doc.add(new Field("yangqikeywords", res.getString("keywords"),Field.Store.YES,Index.ANALYZED));
            doc.add(new Field("yangyiname", res.getString("name"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("yangqiaddress", res.getString("address"),Field.Store.YES,Index.NOT_ANALYZED));
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return doc;
    }
    //给paper添加索引
    public static Document getpaperDocument(ResultSet res)throws Exception {
        Document doc=new Document();
        try{
            doc.add(new Field("paperkeywords", res.getString("name"),Field.Store.YES,Index.ANALYZED));
            doc.add(new Field("pubyear", res.getString("pubyear"),Field.Store.YES,Index.NOT_ANALYZED));
            doc.add(new Field("label", res.getString("UID"),Field.Store.YES,Index.NOT_ANALYZED));
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return doc;
    }


    public static String deleteIndex()
    {
        try {
            String delpath="F:\\IdeaProjects\\lucene\\index";
            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "\\" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                        System.out.println(delfile.getAbsolutePath() + "删除文件成功");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("deletefile() Exception:" + e.getMessage());
        }
        return "删除索引成功";
    }

    public static String deleteyangqiIndex()
    {
        try {
            String delpath="F:\\IdeaProjects\\qq1\\qq\\yangqii_index_test";
            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "\\" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                        System.out.println(delfile.getAbsolutePath() + "删除文件成功");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("deletefile() Exception:" + e.getMessage());
        }
        return "删除索引成功";
    }

    public static String deletepaperIndex()
    {
        try {
            String delpath="F:\\IdeaProjects\\qq1\\qq\\paper_index";
            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "\\" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                        System.out.println(delfile.getAbsolutePath() + "删除文件成功");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("deletefile() Exception:" + e.getMessage());
        }
        return "删除索引成功";
    }
}
