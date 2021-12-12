package com.example.service.impl;

import com.example.bean.Article;
import com.example.bean.Content;
import com.example.bean.Predicate;
import com.example.bean.Relation;
import com.example.dao.ArticleDao;
import com.example.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleDao articleDao;

    @Override
    public void articleInsert(Article article) {
        articleDao.articleInsert(article);
    }

    @Override
    public List<Article> articleList() {
        List<Article> articles = articleDao.articleList();
        return articles;

    }

    @Override
    public void deleteById(int id) {
        articleDao.articleDeleteById(id);
    }

    @Override
    public String selectAddressById(int id) {
        String address = articleDao.selectAddressById(id);
        return address;
    }

    @Override
    public Content selectContent(String content) {
        return articleDao.selectContent(content);

    }

    @Override
    public void contentInsert(String nounid,String content) {
        articleDao.contentInsert(nounid,content);
    }

    @Override
    public Predicate selectPred(String content) {
        return articleDao.selectPred(content);
    }

    @Override
    public void PredInsert(String predicateid,String content) {
        articleDao.PredInsert(predicateid,content);
    }

    @Override
    public void relationInsert(Relation relation) {
        articleDao.relationInsert(relation);
    }

    @Override
    public List<Relation> selectRelationByArticleId(int id) {
        return articleDao.selectRelationByArticleId(id);
    }

    @Override
    public String selectNounContentById(String id) {
        return articleDao.selectNounContentById(id);
    }

    @Override
    public String selectPredicateContentById(String id) {
        return articleDao.selectPredicateContentById(id);
    }

    @Override
    public List<Content> selectAllContent() {
        return articleDao.selectAllContent();
    }

    @Override
    public void updateArticleState(Article article) {
        articleDao.updateArticleState(article);
    }


}
