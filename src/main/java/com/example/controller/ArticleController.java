package com.example.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Utils.*;
import com.example.bean.Article;
import com.example.bean.Content;
import com.example.bean.Predicate;
import com.example.bean.Relation;
import com.example.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 上传文件，并将上传信息存入数据库中
 */
@Controller
@RequestMapping("/article")
public class ArticleController
{

    //    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ArticleController.class);//日志
    @Resource
    ArticleService articleService;

    @PostMapping("/upload")
    @ResponseBody
    public Map<String, String> upload(@RequestParam("file") MultipartFile file, @RequestHeader("token") String token)
    {
        Article article = new Article();
        Map<String, String> map = new HashMap<>();

        if (file.isEmpty())
        {
            map.put("msg", "上传失败,请选择文件!");
            return map;
        }

        String fileName = file.getOriginalFilename();
        long size = file.getSize();
        String type = file.getContentType();
        String filePath = "D:\\Future\\Others\\JavaExercise\\NLPServer\\upload";
        File dest = new File(filePath + fileName);
        try
        {
            file.transferTo(dest);
            map.put("msg", "上传成功！");
            article.setArticleName(fileName); //上传文件名
            article.setArticleSize(size); //文件大小
            article.setArticleType(type); //文件类型
            String time = TimeUtils.nowDate();
            article.setUploadTime(time); //上传时间
            DecodedJWT jwt = JWTUtils.verify(token);
            String username = jwt.getClaim("username").asString();
            article.setUploader(username);//上传人
            article.setArticleAddress(filePath + fileName); //上传地址
            article.setArticleState("未处理");
            articleService.articleInsert(article);
            return map;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        map.put("msg", "上传失败！");
        return map;
    }

    /**
     * 查询所有文章
     *
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public List<Article> list()
    {
        List<Article> article = articleService.articleList();
        return article;
    }

    /**
     * 通过id删除文件及其记录
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Map<String, String> deleteById(@RequestParam("id") int id)
    {
        Map<String, String> map = new HashMap<>();
        String dest = articleService.selectAddressById(id);
        if (dest == null)
        {
            map.put("msg", "删除失败，没有该文件！");
            return map;
        }
        File file = new File(dest);
        if (file.isFile() && file.exists())
        {
            file.delete();
            articleService.deleteById(id);
            map.put("msg", "删除成功！");
            return map;
        }

        map.put("msg", "删除失败！");
        return map;

    }

    @PostMapping("/process")
    @ResponseBody
    public Map<String, Object> processArticle(@RequestParam("id") int id)
    {
        Article article = new Article();
        Map<String, Object> map = new HashMap<>();
        String dest = articleService.selectAddressById(id);
        if (dest == null)
        {
            map.put("msg", "没有该文件！");
            return map;
        }
        article.setArticle_id(id);
        article.setArticleState("正在处理中");
        articleService.updateArticleState(article);
        ArrayList<String> list = FileUtils.FileRead(dest);
        for (String s : list)
        {
            JSONObject jsonObject = NLPUtils.executePython(s);
            if (NLPUtils.checkImportance(jsonObject))
            {
                JSONArray entities = NLPUtils.getEntities(jsonObject);
                for (int i = 0; i < entities.size(); i++)
                { //遍历实体
                    JSONArray entitiesJSONArray = entities.getJSONArray(i);
                    Relation relation = new Relation();
                    for (int j = 0; j < entitiesJSONArray.size(); j++)
                    { //实体内部信息
                        JSONArray jsonArray = entitiesJSONArray.getJSONArray(j);
                        String jsonArrayString = jsonArray.getString(1);
                        if (jsonArrayString.equals("ARG0"))
                        {
                            Content content = articleService.selectContent(jsonArray.getString(0));
                            if (content == null)
                            {
                                String s1 = UUID.randomUUID().toString().replace("-", "");
                                articleService.contentInsert(s1, jsonArray.getString(0));
                                relation.setSubjectid(s1);
                            }
                            else
                            {
                                relation.setSubjectid(content.getNounid());
                            }
                        }
                        else if (jsonArrayString.equals("PRED"))
                        {
                            Predicate predicate = articleService.selectPred(jsonArray.getString(0));
                            if (predicate == null)
                            {
                                String s1 = UUID.randomUUID().toString().replace("-", "");
                                articleService.PredInsert(s1, jsonArray.getString(0));
                                relation.setPredicateid(s1);
                            }
                            else
                            {
                                relation.setPredicateid(predicate.getPredicateid());
                            }
                        }
                        else if (jsonArrayString.equals("ARG1") ||
                                jsonArrayString.equals("ARG2") ||
                                jsonArrayString.equals("ARG3") ||
                                jsonArrayString.equals("ARG4") ||
                                jsonArrayString.equals("ARG5"))
                        {
                            Content content = articleService.selectContent(jsonArray.getString(0));
                            if (content == null)
                            {
                                String s1 = UUID.randomUUID().toString().replace("-", "");
                                articleService.contentInsert(s1, jsonArray.getString(0));
                                switch (jsonArrayString)
                                {
                                    case "ARG1":
                                        relation.setObjectid(s1);
                                        break;
                                    case "ARG2":
                                        relation.setObjectid2(s1);
                                        break;
                                    case "ARG3":
                                        relation.setObjectid3(s1);
                                        break;
                                    case "ARG4":
                                        relation.setObjectid4(s1);
                                        break;
                                    case "ARG5":
                                        relation.setObjectid5(s1);
                                        break;
                                }
                            }
                            else
                            {
                                relation.setObjectid(content.getNounid());
                            }
                        }
                        else if (jsonArrayString.equals("ARGM-TMP"))
                        {
                            relation.setDate(jsonArray.getString(0));
                        }
                    }
                    relation.setAdverbial(String.valueOf(id)); //状语备用看作文章id
                    articleService.relationInsert(relation);
                }
            }
        }
        article.setArticleState("处理完毕");
        articleService.updateArticleState(article);
        map.put("msg", "插入成功");
        return map;
    }

    @PostMapping("/timu")
    @ResponseBody
    public List<Map<String, Object>> setTopic(@RequestParam("question_num") int num, @RequestParam("article_id") int id)
    {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        String dest = articleService.selectAddressById(id);
        if (dest == null)
        {
            map.put("msg", "没有该文件！");
            map.put("result", false);
            list.add(map);
            return list;
        }
        List<Relation> relationList = articleService.selectRelationByArticleId(id);
        if (num > relationList.size())
        {
            map.put("msg", "出题数过多，请减少，最大出题数：" + relationList.size());
            map.put("result", false);
            list.add(map);
            return list;
        }
        int[] randomNumber = RandomUtils.randomNumber(num, relationList);
        int index = 1;
        for (int rand : randomNumber)
        {
            Map<String, Object> map1 = new HashMap<>();
            if (rand == relationList.size())
            {
                rand = rand - 1;
            }
            Relation relation = relationList.get(rand); //得到其中一个关系
            String subjectContent = articleService.selectNounContentById(relation.getSubjectid());
            String predicateContent = articleService.selectPredicateContentById(relation.getPredicateid());
            String objectContent1 = null;
            String objectContent2 = null;
            String objectContent3 = null;
            String objectContent4 = null;
            String objectContent5 = null;
            if (relation.getObjectid() != null)
            {
                objectContent1 = articleService.selectNounContentById(relation.getObjectid());
            }
            else if (relation.getObjectid2() != null)
            {
                objectContent2 = articleService.selectNounContentById(relation.getObjectid2());
            }
            else if (relation.getObjectid3() != null)
            {
                objectContent3 = articleService.selectNounContentById(relation.getObjectid3());
            }
            else if (relation.getObjectid4() != null)
            {
                objectContent4 = articleService.selectNounContentById(relation.getObjectid4());
            }
            else if (relation.getObjectid5() != null)
            {
                objectContent5 = articleService.selectNounContentById(relation.getObjectid5());
            }
            List<String> strList = new ArrayList<>(); //答案集合
            String objectContent = "";
            StringBuffer sb = new StringBuffer(subjectContent); //题目字符串
            sb.append(predicateContent);
            if (objectContent1 != null)
            {
                objectContent = objectContent1;
                sb.append(objectContent);
            }
            else if (objectContent2 != null)
            {
                objectContent = objectContent2;
                sb.append(objectContent);
            }
            else if (objectContent3 != null)
            {
                objectContent = objectContent3;
                sb.append(objectContent);
            }
            else if (objectContent4 != null)
            {
                objectContent = objectContent4;
                sb.append(objectContent);
            }
            else if (objectContent5 != null)
            {
                objectContent = objectContent5;
                sb.append(objectContent);
            }
            if (RandomUtils.chooseNumber(2) == 0)
            { //当返回值为0的时候，去掉subject
                sb.replace(0, subjectContent.length(), "()");
                strList.add(subjectContent);
                List<Content> contents = articleService.selectAllContent();
                contents.removeIf(content -> content.getContent().equals(subjectContent));
                int[] contentNumber = RandomUtils.randomContentNumber(3, contents);
                for (int i : contentNumber)
                {
                    if (i == contents.size())
                    {
                        i = i - 1;
                    }
                    strList.add(contents.get(i).getContent());
                }

            }
            else
            { //当返回值为1的时候，去掉object
                sb.replace(subjectContent.length() + predicateContent.length(), sb.length(), "()");
                strList.add(objectContent);
                List<Content> contents = articleService.selectAllContent();
                String finalObjectContent = objectContent;
                contents.removeIf(content -> content.getContent().equals(finalObjectContent));
                int[] contentNumber = RandomUtils.randomContentNumber(3, contents);
                for (int i : contentNumber)
                {
                    if (i == contents.size())
                    {
                        i = i - 1;
                    }
                    strList.add(contents.get(i).getContent());
                }
            }
            map1.put("题目题干", sb);
            map1.put("答案", strList.get(0)); //集合中的第一个是答案
            Collections.shuffle(strList); //打乱集合顺序
            map1.put("题目选项", strList); //打乱后的选项
            list.add(map1);
            index++;
        }
        return list;
    }
}
